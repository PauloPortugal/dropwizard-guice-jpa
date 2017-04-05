package com.pmonteiro.dropwizard.dao;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;

@Transactional
public class TaskDAO extends BaseDAO {

    @Inject
    protected TaskDAO(final Provider<EntityManager> entityManager) {
        super(entityManager);
    }
}
