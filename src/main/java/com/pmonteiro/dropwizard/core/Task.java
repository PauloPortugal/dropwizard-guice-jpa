package com.pmonteiro.dropwizard.core;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import com.pmonteiro.dropwizard.api.TaskApi;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.net.URI;

@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "tasks_id_seq", sequenceName = "tasks_id_seq", allocationSize = 1)
@ApiModel(value="Task Entity", description = "A description of a task")
public class Task extends AbstractEntity implements HalResource {

    @ApiModelProperty(example = "Lorem Ipsum, porro quisquam est qui dolorem ipsum quia dolo")
    private String description;

    public Task() {}

    public Task(String description) {
        this.description = description;
    }

    public Task(TaskApi task) {
        this.description = task.getDescription();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public HalRepresentation.HalRepresentationBuilder representationBuilder() {
        return HalRepresentation.builder()
                .addProperty("id", this.getId())
                .addProperty("description", this.getDescription())
                .addLink("self", this);
    }

    @Override
    public URI location() {
        return super.location("/tasks/" + getId());
    }
}

