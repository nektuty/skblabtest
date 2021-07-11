package ru.tshagaev.skblabtest.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Состояние учетки пользователя системы
 * NOTE По хорошему, тут нужна отдельная сущность, а не перечисление
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@AllArgsConstructor
public enum UserState {
    NEW("Cоздана"),
    ON_APPROVAL("На одобрении"),
    APPROVED("Одобрена"),
    REJECTED("Отклонена");

    @Getter
    private final String title;
}