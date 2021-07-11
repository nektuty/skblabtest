package ru.tshagaev.skblabtest.service.messaging;

import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.tshagaev.skblabtest.domain.auth.UserState;
import ru.tshagaev.skblabtest.domain.messaging.MessageId;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Заглушка для стороннего сервиса
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Service
public class MessagingServiceStub implements MessagingService {

    @Override
    public <T> MessageId send(Message<T> msg) {
        return new MessageId(UUID.randomUUID());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Message<T> receive(MessageId messageId) throws TimeoutException {
        if (shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if (shouldSleep()) {
            sleep();
        }

        var userState = shouldApprove() ? UserState.APPROVED : UserState.REJECTED;
        return (Message<T>) MessageBuilder.withPayload(userState).build();
    }

    @Override
    public <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException {
        final MessageId messageId = send(request);
        return receive(messageId);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldApprove() {
        return new Random().nextInt(10) > 3;
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
