package com.tn.dw.integration;

import com.tn.dw.ConfigDecrypter;
import com.tn.dw.EncryptionClient;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ServerCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.function.Function;

/**
 * Intercept the config before it's used in order to decrypt encrypted values.
 */
public class ConfigDecryptingServerCommand<T extends Configuration> extends ServerCommand<T> {
    private final Function<T, EncryptionClient> encClientFunc;

    public ConfigDecryptingServerCommand(Application<T> application, Function<T, EncryptionClient> encClientFunc) {
        super(application, "encserver", "Runs the Dropwizard application as an HTTP server, decrypting config keys");
        this.encClientFunc = encClientFunc;
    }

    @Override
    protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
        EncryptionClient keyClient = encClientFunc.apply(configuration);
        ConfigDecrypter decrypter = new ConfigDecrypter(keyClient);
        decrypter.decrypt(configuration);
        super.run(bootstrap, namespace, configuration);
    }
}
