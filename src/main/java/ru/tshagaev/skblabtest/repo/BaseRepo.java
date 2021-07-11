package ru.tshagaev.skblabtest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.tshagaev.skblabtest.domain.BaseEntity;

/**
 * Базовый репозиторий сущностей домена
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@NoRepositoryBean
public interface BaseRepo<E extends BaseEntity> extends JpaRepository<E, Long> {
}
