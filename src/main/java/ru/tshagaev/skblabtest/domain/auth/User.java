package ru.tshagaev.skblabtest.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.tshagaev.skblabtest.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Учетка пользователя системы
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Entity
@Table(name = "user_t")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends BaseEntity {
    @NotBlank(message = "Логин обязателен.")
    @Column(name = "username_p", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Пароль обязателен.")
    @Column(name = "password_p", nullable = false)
    private String password;

    @NotBlank(message = "Адрес электронной почты обязателен.")
    @Column(name = "email_p", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Имя обязательно.")
    @Column(name="first_name_p", nullable = false)
    private String firstName;

    @Column(name="mid_name_p")
    private String midName;

    @NotBlank(message = "Фамилия обязательна.")
    @Column(name="last_name_p", nullable = false)
    private String lastName;

    @Column(name="state_p", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state = UserState.NEW;

    @Transient
    private String passwordConfirmation;
}
