package com.tn.dw;

public interface EncryptionClient {
    String encrypt(String clearText);

    String decrypt(String encrypted);
}
