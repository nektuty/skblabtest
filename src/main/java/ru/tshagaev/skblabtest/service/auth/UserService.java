package ru.tshagaev.skblabtest.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tshagaev.skblabtest.domain.auth.User;
import ru.tshagaev.skblabtest.exception.UserAlreadyExistException;
import ru.tshagaev.skblabtest.repo.auth.UserRepo;
import ru.tshagaev.skblabtest.service.BaseService;

/**
 * Сервис для учетки пользователя системы
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Slf4j
@Service
public class UserService extends BaseService<User, UserRepo> {
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo repo, PasswordEncoder passwordEncoder) {
        super(repo);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрация новой учетки
     *
     * @param user Учетка
     */
    public void register(User user) {
        if (repo.findByUsernameOrEmail(user.getUsername(), user.getEmail()) != null) {
            throw new UserAlreadyExistException();
        }

        // Зашифруем пароль и подтверждение (подтверждение не сохраняется в БД, но нужно для валидации при сохранении)
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setPasswordConfirmation(encodedPassword);
        save(user);
        log.info("Учетная запись '" + user.getUsername() + "' сохранена.");
    }
}
