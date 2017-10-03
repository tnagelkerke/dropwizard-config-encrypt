package com.tn.dw.integration;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;

public abstract class ConfigDecryptingApplication<T extends ConfigDecryptingConfiguration> extends Application<T> {
    @Override
    public void initialize(Bootstrap<T> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new DecryptionConfiguredBundle<>(ConfigDecryptingConfiguration::getKeyEncryptionClient));
    }
}
