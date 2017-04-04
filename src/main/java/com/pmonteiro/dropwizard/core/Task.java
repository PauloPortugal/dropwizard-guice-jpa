package com.pmonteiro.dropwizard.core;

import javax.persistence.Entity;

@Entity
public class Task extends AbstractEntity {

    private String description;

    public Task() {}

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
