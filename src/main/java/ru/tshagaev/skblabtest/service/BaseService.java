package ru.tshagaev.skblabtest.service;

import ru.tshagaev.skblabtest.domain.BaseEntity;
import ru.tshagaev.skblabtest.repo.BaseRepo;

import javax.persistence.EntityNotFoundException;

/**
 * Базовый сервис сущностей домена
 *
 * @author tshagaev
 * @since 11.07.2021
 */
public abstract class BaseService<E extends BaseEntity, R extends BaseRepo<E>> {
    protected R repo;

    public BaseService(R repo) {
        this.repo = repo;
    }

    public void save(E entity) {
        repo.save(entity);
    }

    public void delete(E entity) {
        repo.delete(entity);
    }

    public E getById(Long id) {
        return repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
