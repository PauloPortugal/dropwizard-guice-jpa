package com.pmonteiro.dropwizard.core;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "tasks_id_seq", sequenceName = "tasks_id_seq", allocationSize = 1)
public class Task extends AbstractEntity {

    private String description;

    private Task() {}

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
