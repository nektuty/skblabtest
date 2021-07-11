package ru.tshagaev.skblabtest.exception;

/**
 * @author tshagaev
 * @since 11.07.2021
 */
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("Пользователь с указанным логином или адресом электронной почты уже существует.");
    }
}
