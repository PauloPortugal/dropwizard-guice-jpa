package com.pmonteiro.dropwizard.dao;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.pmonteiro.dropwizard.api.TaskApi;
import com.pmonteiro.dropwizard.core.Task;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

@Transactional
public class TaskDAO extends BaseDAO<Task> {

    @Inject
    public TaskDAO(final Provider<EntityManager> entityManager) {
        super(entityManager);
    }

    public Optional<Task> findById(final Long id) {
        return findById(Task.class, id);
    }

    public Optional<Integer> update(final TaskApi task, Long id) {
        Query query = getEntityManager()
                .createQuery("UPDATE Task SET description=:description WHERE id = :id")
                .setParameter("description", task.getDescription())
                .setParameter("id", id);
        return Optional.of(query.executeUpdate());
    }
}
