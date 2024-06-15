package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.service.interfaces.ConfirmationService;
import de.ait_tr.g_40_shop.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service // т.к. это сервис
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender; // объект типа JavaMailSender умеет отправлять письма на эмейл
    private final Configuration mailConfig; // умеет конфигурировать отправку писем

    private final ConfirmationService confirmationService; // EmailService должен обращаться к ConfirmationService,
    // чтобы получить код подтверждения для пользователя

    public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig, ConfirmationService confirmationService) {
        this.sender = sender;
        this.mailConfig = mailConfig;
        this.confirmationService = confirmationService;

        mailConfig.setDefaultEncoding("UTF-8"); // кодировка по умолчанию, которая используется для писем
        mailConfig.setTemplateLoader(
                new ClassTemplateLoader(EmailServiceImpl.class, "/mail/") // указываем, где у
                // нас хранятся шаблоны писем - в папке mail в resources
        );
    }

    @Override
    public void sendConfirmationEmail(User user) { // метод занимается отправкой письма юзеру

        MimeMessage message = sender.createMimeMessage(); // с помощью класса MimeMessage создаем объект письма
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // создаем помощника, который будет помогать наполнять письмо
        String text = generateConfirmationEmail(user); // обращаемся к методу, который занимается генерацией письма, чтобы получить текст письма
        try {
            helper.setFrom("daria.iavtushenko@gmail.com"); // от кого письмо будет направлено пользователю
            helper.setTo(user.getEmail()); // кому письмо
            helper.setSubject("Registration"); // задаем тему письма
            helper.setText(text, true); // вставляем сам текст письма в формате html (это указано в параметре html:true)
            sender.send(message); // на объекте sender вызываем метод send и передаем туда письмо - письмо будет отправлено

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateConfirmationEmail(User user) { // генерация письма
        try {
            Template template = mailConfig.getTemplate("confirm_mail.ftlh"); // создаем шаблон письма, входящий
            // параметр - имя файла с шаблоном письма
            String code = confirmationService.generateConfirmationCode(user); // передаем пользователя в метод для генерации кода подтверждения

            String url = "http://localhost:8080/register?code=" + code; // на этот адрес пользователь будет отправлять гет запрос,
            // нажиая на ссылку, пришедшую по эмейл (в запросе код, который получил польз), чтобы подтвердить свою регистрацию.
            // Эндпоинт придумываем сами. При помощи параметра code= отправляет пользователь код в гет запросе, нажимая на ссылку из письма
            // Потом этот код будет сверяться с тем, что уже есть в базе данных

            Map<String, Object> templateMap = new HashMap<>(); // при помощи Map вставляем значения в шаблон
            templateMap.put("name", user.getUsername()); // наполняем мапу
            templateMap.put("link", url);

            // метод processTemplateIntoString берет шаблон письма template, наполняет его данными (name и link) из templateMap,
            // и возвращаем сгенерированный текст письма:

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMap);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
