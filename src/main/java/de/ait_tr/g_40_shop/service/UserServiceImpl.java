package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.UserRepository;
import de.ait_tr.g_40_shop.service.interfaces.EmailService;
import de.ait_tr.g_40_shop.service.interfaces.RoleService;
import de.ait_tr.g_40_shop.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService) {
        this.repository = repository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User %s not found", username)));

         // Альтернативный вариант
//        User user = repository.findByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(
//                    String.format("User %s not found", username));
//        }
//
//        return user;
    }

    @Override
    public void register(User user) { // метод для регистрации новых пользователей. Нам на вход приходит юзер
        user.setId(null); // чтобы нам не пердали на вход id, мы обнуляем id юзера
        user.setPassword(encoder.encode(user.getPassword())); // шифруем пароль пользователя, т.к. он присылает нам пароль в сыром виде
        user.setActive(false); // юзер станет активным только когда подтвердит свою регистрацию  через эмейл
        user.setRoles(Set.of(roleService.getRoleUser())); // передаем пользователю его роль

        repository.save(user); // сохраняем юзера в БД
        emailService.sendConfirmationEmail(user); // отправляем юзеру эмейл с кодом подтверждения

    }
}
