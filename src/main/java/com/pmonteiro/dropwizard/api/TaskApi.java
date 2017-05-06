package com.pmonteiro.dropwizard.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="A new task", description = "A description of a task")
public class TaskApi {

    @ApiModelProperty(example = "Lorem Ipsum, porro quisquam est qui dolorem ipsum quia dolo")
    private String description;

    private TaskApi() {}

    public TaskApi(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
