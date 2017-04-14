package com.pmonteiro.dropwizard.dao;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseDAO<T> {
    private final Provider<EntityManager> entityManager;

    protected BaseDAO(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<List> find(final Class<T> clazz) {
        return Optional.of(entityManager.get().createQuery("Select t from " + clazz.getSimpleName() + " t").getResultList());
    }

    public void persist(final T object) {
        entityManager.get().persist(object);
    }

    public void remove(final T object) {
        entityManager.get().remove(object);
    }

    public <ID> Optional<T> findById(final Class<T> clazz, final ID id) {
        return Optional.ofNullable(entityManager.get().find(clazz, id));
    }

    public Optional<T> merge(final T object) {
        return Optional.ofNullable(entityManager.get().merge(object));
    }

    public EntityManager getEntityManager() {
        return entityManager.get();
    }

}
