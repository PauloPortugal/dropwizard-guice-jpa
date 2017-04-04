package com.pmonteiro.dropwizard;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pmonteiro.dropwizard.db.PersistInitialiser;
import com.pmonteiro.dropwizard.resources.TasksResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropwizardGuiceApplication extends Application<DropwizardGuiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardGuiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardGuice";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardGuiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DropwizardGuiceConfiguration configuration, final Environment environment) {
        final Injector injector = Guice.createInjector(new DropwizardGuiceModule(configuration, environment));
        environment.jersey().register(injector.getInstance(TasksResource.class));

        injector.getInstance(PersistInitialiser.class);
    }

}
