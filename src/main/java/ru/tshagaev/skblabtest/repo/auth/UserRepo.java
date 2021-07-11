package ru.tshagaev.skblabtest.repo.auth;

import org.springframework.stereotype.Repository;
import ru.tshagaev.skblabtest.domain.auth.User;
import ru.tshagaev.skblabtest.repo.BaseRepo;

/**
 * Репозиторий для учетки пользователя системы
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Repository
public interface UserRepo extends BaseRepo<User> {
    User findByUsernameOrEmail(String username, String email);
}
