package ru.tshagaev.skblabtest.service.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tshagaev.skblabtest.domain.auth.User;
import ru.tshagaev.skblabtest.domain.auth.UserState;
import ru.tshagaev.skblabtest.domain.messaging.MessageId;
import ru.tshagaev.skblabtest.repo.messaging.MessageIdRepo;
import ru.tshagaev.skblabtest.service.BaseService;
import ru.tshagaev.skblabtest.service.auth.UserService;
import ru.tshagaev.skblabtest.service.mail.EmailAddress;
import ru.tshagaev.skblabtest.service.mail.EmailContent;
import ru.tshagaev.skblabtest.service.mail.SendMailer;
import ru.tshagaev.skblabtest.util.ExecutorUtils;

import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Сервис для отправки сообщений и обработки ответов
 *
 * NOTE Подозреваю, что для работы с сообщениями нужно было использовать стандартные возможности брокеров сообщений.
 *  Но, поскольку опыта работы с ними у меня нет, то нагородил свой велосипед из того, во что умею.
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Slf4j
@Service
public class MessageIdService extends BaseService<MessageId, MessageIdRepo> {
    private final MessagingService messagingService;
    private final UserService userService;
    private final SendMailer mailer;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public MessageIdService(MessageIdRepo repo, MessagingService messagingService, UserService userService, SendMailer mailer) {
        super(repo);
        this.messagingService = messagingService;
        this.userService = userService;
        this.mailer = mailer;
    }

    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdownNow();
    }

    public void sendForApproval(User user) {
        var message = MessageBuilder.withPayload(user).build();
        var messageId = messagingService.send(message);
        // При отправке сообщения сохраняем его ID в БД вместе с ID учетки
        messageId.setUser(user);
        save(messageId);
        log.info("Сообщение " + messageId.getUuid() + " для учетной записи '" + user.getUsername() + "' отправлено.");
    }

    /**
     * Интервал запуска - одна минута.
     */
    @Scheduled(fixedDelayString = "PT1M")
    public void receiveMessages() {
        // Получаем сообщения для необработанных событий и обработанных с ошибкой.
        log.info("Получение сообщений начато.");
        var notAnsweredList = repo.findAllNotAnswered();
        ExecutorUtils.runTaskForList(notAnsweredList, this::receiveMessage, executor);
        log.info("Получение сообщений закончено.");

        // Получаем сообщения для обработанных событий с неотправленными письмами.
        log.info("Отправка писем начата.");
        var allAnswered = repo.findAllAnswered();
        ExecutorUtils.runTaskForList(allAnswered, this::sendMail, executor);
        log.info("Отправка писем закончена.");
    }

    private void receiveMessage(MessageId messageId) {
        Message<UserState> receive;
        try {
            // Пробуем получить ответ от шины
            receive = messagingService.receive(messageId);
        } catch (TimeoutException e) {
            // Если отвалились по таймауту, залогируем и попробуем в следующий раз
            log.info("Ошибка обработки сообщения " + messageId.getUuid() + " - ", e);
            return;
        }

        // Получили, обновляем и сохраняем учетку
        var userState = receive.getPayload();
        User user;
        try {
            user = userService.getById(messageId.getUser().getId());
        } catch (EntityNotFoundException e) {
            // Учетка куда-то делась, залогируем и удалим сообщение (пробовать в следующий раз смысла нет)
            log.info("Ошибка обработки сообщения " + messageId.getUuid() + " - ", e);
            delete(messageId);
            return;
        }

        user.setState(userState);
        userService.save(user);
        log.info("Учетная запись пользователя '" + user.getUsername() + "' " + userState.getTitle().toLowerCase());

        // Проставляем флаг обработки
        messageId.setAnswered(true);
        save(messageId);
    }

    private void sendMail(MessageId messageId) {
        var user = messageId.getUser();
        String message;
        switch (user.getState()) {
            case APPROVED:
                message = "Учетная запись одобрена.";
                break;
            case REJECTED:
                message = "Учетная запись отклонена.";
                break;
            default:
                message = "Некорректный статус учетной записи: " + user.getState();
        }

        try {
            mailer.sendMail(EmailAddress.of(user.getEmail()), EmailContent.of(message));
        } catch (TimeoutException e) {
            // Если отвалились по таймауту, залогируем и попробуем в следующий раз
            log.info("Ошибка отправки письма для сообщения " + messageId.getUuid() + " - ", e);
            return;
        }

        // Отправка прошла, удаляем запись
        log.info("Письмо для сообщения " + messageId.getUuid() + " ( " + user.getEmail() + " ) отправлено.");
        delete(messageId);
    }
}
