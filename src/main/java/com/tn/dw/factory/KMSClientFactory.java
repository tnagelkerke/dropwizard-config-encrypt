package com.tn.dw.factory;

import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tn.dw.EncryptionClient;
import com.tn.dw.KMSEncryptionClient;

import javax.validation.constraints.NotNull;

@JsonTypeName("kms")
public class KMSClientFactory implements EncryptionClientFactory {

    @JsonProperty
    @NotNull
    private Regions regions;

    @JsonProperty
    @NotNull
    private String key;

    @Override
    public EncryptionClient build() {
        return new KMSEncryptionClient(regions, key);
    }
}
