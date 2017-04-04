package com.pmonteiro.dropwizard;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;

import java.util.Properties;

public class DropwizardGuiceModule extends AbstractModule {

    private DropwizardGuiceConfiguration configuration;
    private Environment environment;

    public DropwizardGuiceModule(final DropwizardGuiceConfiguration configuration, final Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(DropwizardGuiceConfiguration.class).toInstance(configuration);
        bind(Environment.class).toInstance(environment);

        install(jpaModule(configuration.getDataSourceFactory()));
    }

    private Module jpaModule(DataSourceFactory dataSourceFactory ) {
        final Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.driver", dataSourceFactory.getDriverClass());
        properties.put("javax.persistence.jdbc.url", dataSourceFactory.getUrl());
        properties.put("javax.persistence.jdbc.user", dataSourceFactory.getUser());
        properties.put("javax.persistence.jdbc.password", dataSourceFactory.getPassword());

        final JpaPersistModule jpaModule = new JpaPersistModule("DefaultUnit");
        jpaModule.properties(properties);

        return jpaModule;
    }
}
