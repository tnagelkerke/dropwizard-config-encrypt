package com.tn.dw.factory;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tn.dw.EncryptionClient;

@JsonTypeName("none")
public class NoEncryptionClientFactory implements EncryptionClientFactory {

    private static final EncryptionClient NOP_KEY_CLIENT = new EncryptionClient() {
        @Override
        public String encrypt(String clearText) {
            return clearText;
        }

        @Override
        public String decrypt(String encrypted) {
            return encrypted;
        }
    };

    @Override
    public EncryptionClient build() {
        return NOP_KEY_CLIENT;
    }
}
