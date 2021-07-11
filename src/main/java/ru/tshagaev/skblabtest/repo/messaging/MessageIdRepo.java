package ru.tshagaev.skblabtest.repo.messaging;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tshagaev.skblabtest.domain.messaging.MessageId;
import ru.tshagaev.skblabtest.repo.BaseRepo;

import java.util.List;

/**
 * Репозиторий для ID сообщений, отправляемого в сторонний сервис
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Repository
public interface MessageIdRepo extends BaseRepo<MessageId> {
    /**
     * @return Список ID сообщений, на которые пока не получен ответ от сервиса.
     */
    @Query("select m from MessageId m where m.answered = false")
    List<MessageId> findAllNotAnswered();

    /**
     * @return Список ID сообщений, на которые ответ от сервиса получен, но письмо ещё не отправлено.
     */
    @Query("select m from MessageId m where m.answered = true ")
    List<MessageId> findAllAnswered();
}
