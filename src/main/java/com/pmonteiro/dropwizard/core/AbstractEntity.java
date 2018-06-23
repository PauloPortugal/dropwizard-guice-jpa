package com.pmonteiro.dropwizard.core;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.ws.rs.WebApplicationException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1")
    private Long id;

    public Long getId() {
        return id;
    }

    protected URI location(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new WebApplicationException("Could not create URI for task " + getId(), e);
        }
    }
}