package ru.tshagaev.skblabtest.domain.messaging;

import lombok.*;
import ru.tshagaev.skblabtest.domain.BaseEntity;
import ru.tshagaev.skblabtest.domain.auth.User;

import javax.persistence.*;
import java.util.UUID;

/**
 * ID сообщения, отправляемого в сторонний сервис.
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Entity
@Table(name = "message_id_t")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageId extends BaseEntity {
    @Column(name="uuid_p", nullable = false)
    private UUID uuid;
    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private User user;
    @Column(name="answered_p", nullable = false)
    private boolean answered = false;

    public MessageId(UUID uuid) {
        this.uuid = uuid;
    }
}
