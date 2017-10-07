package com.tn.dw;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test {@link KMSEncryptionClient}: interaction with AWSKMS and Base64 encoding.
 */
public class KMSEncryptionClientTest {
    @Test
    public void encrypt() throws Exception {
        AWSKMS mock = mock(AWSKMS.class);
        EncryptResult encryptResult = new EncryptResult();
        encryptResult.setCiphertextBlob(ByteBuffer.wrap("myEncryptedPassword".getBytes()));
        when(mock.encrypt(any(EncryptRequest.class))).thenReturn(encryptResult);

        KMSEncryptionClient kmsEncryptionClient = new KMSEncryptionClient(mock, "123");
        String encrypted = kmsEncryptionClient.encrypt("myPassword");

        assertEquals(Base64.getEncoder().encodeToString("myEncryptedPassword".getBytes()), encrypted);
        verify(mock, times(1)).encrypt(any(EncryptRequest.class));
    }

    @Test
    public void decrypt() throws Exception {
        AWSKMS mock = mock(AWSKMS.class);
        DecryptResult decryptResult = new DecryptResult();
        decryptResult.setPlaintext(ByteBuffer.wrap("myPassword".getBytes()));
        when(mock.decrypt(any(DecryptRequest.class))).thenReturn(decryptResult);

        KMSEncryptionClient kmsEncryptionClient = new KMSEncryptionClient(mock, "123");
        String decrypted = kmsEncryptionClient.decrypt(Base64.getEncoder().encodeToString("myEncryptedPassword".getBytes()));

        assertEquals("myPassword", decrypted);
        verify(mock, times(1)).decrypt(any(DecryptRequest.class));
    }

}