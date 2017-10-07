package com.tn.dw.integration;

import com.tn.dw.ConfigDecrypter;
import com.tn.dw.EncryptionClient;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.function.Function;

public class DecryptionConfiguredBundle<T extends Configuration> implements ConfiguredBundle<T> {
    private final Function<T, EncryptionClient> encClientFunc;

    public DecryptionConfiguredBundle(Function<T, EncryptionClient> encClientFunc) {
        this.encClientFunc = encClientFunc;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        EncryptionClient keyClient = encClientFunc.apply(configuration);
        ConfigDecrypter decrypter = new ConfigDecrypter(keyClient);
        decrypter.decrypt(configuration);
    }

    @Override
    public void initialize(Bootstrap bootstrap) {}
}
