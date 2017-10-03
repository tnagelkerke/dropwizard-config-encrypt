package com.tn.dw.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tn.dw.EncryptionClient;
import com.tn.dw.factory.EncryptionClientFactory;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class ConfigDecryptingConfiguration extends Configuration {
    @NotNull
    @JsonProperty
    private EncryptionClientFactory keyEncryption;

    public EncryptionClient getKeyEncryptionClient() {
        return keyEncryption.build();
    }
}
