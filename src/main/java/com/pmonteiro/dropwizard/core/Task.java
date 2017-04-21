package com.pmonteiro.dropwizard.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "tasks_id_seq", sequenceName = "tasks_id_seq", allocationSize = 1)
@ApiModel(value="Task Entity", description = "A description of a task")
public class Task extends AbstractEntity {

    @ApiModelProperty(example = "Lorem Ipsum, porro quisquam est qui dolorem ipsum quia dolo")
    private String description;

    public Task() {}

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
