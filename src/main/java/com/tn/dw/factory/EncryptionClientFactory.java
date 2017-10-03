package com.tn.dw.factory;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tn.dw.EncryptionClient;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NoEncryptionClientFactory.class)
public interface EncryptionClientFactory extends Discoverable {
    EncryptionClient build();
}