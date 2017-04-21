package com.pmonteiro.dropwizard.core;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    /**
     * For JPA purposes
     */
    public AbstractEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1")
    private Long id;

    public Long getId() {
        return id;
    }
}