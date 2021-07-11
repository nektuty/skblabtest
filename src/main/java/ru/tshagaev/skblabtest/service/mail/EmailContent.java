package ru.tshagaev.skblabtest.service.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * Обертка над содержимым письма (в данном случае только текст).
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Getter
@Setter
public class EmailContent {
    private final String body;

    public static EmailContent of(String body) {
        return new EmailContent(body);
    }

    private EmailContent(String body) {
        this.body = body;
    }
}
