package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.ConfirmationCode;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ExpiredConfirmationCodeException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.FourthTestException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.InvalidConfirmationCodeException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ThirdTestException;
import de.ait_tr.g_40_shop.repository.ConfirmationCodeRepository;
import de.ait_tr.g_40_shop.repository.UserRepository;
import de.ait_tr.g_40_shop.service.interfaces.EmailService;
import de.ait_tr.g_40_shop.service.interfaces.RoleService;
import de.ait_tr.g_40_shop.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;

    private final ConfirmationCodeRepository confirmationCodeRepository;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService, ConfirmationCodeRepository confirmationCodeRepository) {
        this.repository = repository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
        this.confirmationCodeRepository = confirmationCodeRepository;
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
    public void activate(String code) { // активация юзера после его перехода по ссылке
        ConfirmationCode confirmationCode;
        Optional<ConfirmationCode> confirmationCodeOptional = confirmationCodeRepository.findByCode(code);

        if (confirmationCodeOptional.isPresent()) {
            confirmationCode = confirmationCodeOptional.get();
        }
        else {
            throw new InvalidConfirmationCodeException("Confirmation code is invalid");
        }

        if (confirmationCode.getExpired().isAfter(LocalDateTime.now())) {
            User user = confirmationCode.getUser();
            user.setActive(true);
            repository.save(user);
        }
        else {
            throw new ExpiredConfirmationCodeException("Confirmation code is expired");
        }
    }

}
