package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.ConfirmationCode;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.ConfirmationCodeRepository;
import de.ait_tr.g_40_shop.service.interfaces.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().minusMinutes(2);
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expired, user); // это из конструктора класса ConfirmationCode
        repository.save(confirmationCode); //сервер генерирует код для пользователя и сохраняет его в БД
        return code;

    }
}
