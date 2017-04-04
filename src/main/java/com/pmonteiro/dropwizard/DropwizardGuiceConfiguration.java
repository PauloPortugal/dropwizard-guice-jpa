package com.pmonteiro.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class DropwizardGuiceConfiguration extends Configuration {

    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory;

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

}
