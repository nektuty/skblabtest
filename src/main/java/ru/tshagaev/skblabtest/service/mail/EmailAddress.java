package ru.tshagaev.skblabtest.service.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * Обертка над заголовками письма (в данном случае только адрес).
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Getter
@Setter
public class EmailAddress {
    private final String email;

    public static EmailAddress of(String email) {
        return new EmailAddress(email);
    }

    private EmailAddress(String email) {
        this.email = email;
    }
}
