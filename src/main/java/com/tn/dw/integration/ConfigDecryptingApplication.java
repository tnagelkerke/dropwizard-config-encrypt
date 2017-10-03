package com.tn.dw.integration;

import com.tn.dw.EncryptionClient;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;

import java.util.function.Function;

public abstract class ConfigDecryptingApplication<T extends Configuration> extends Application<T> {
    @Override
    public void initialize(Bootstrap<T> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new DecryptionConfiguredBundle<>(getEncryptionClient()));
    }

    abstract Function<T, EncryptionClient> getEncryptionClient();
}
