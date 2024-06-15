package de.ait_tr.g_40_shop.service.interfaces;

import de.ait_tr.g_40_shop.domain.entity.User;

public interface EmailService {

    void sendConfirmationEmail(User user); // задача этого метода - отправить
    // на ящик пользователю письмо с подтверждением регистрации
}
