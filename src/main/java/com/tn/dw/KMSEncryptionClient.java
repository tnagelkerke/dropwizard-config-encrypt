package com.tn.dw;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.AWSKMSException;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Base64;

public class KMSEncryptionClient implements EncryptionClient {
    private static final Logger LOG = LoggerFactory.getLogger(KMSEncryptionClient.class);

    private final AWSKMS awsKmsClient;
    private final String key;

    KMSEncryptionClient(AWSKMS awsKmsClient, String key) {
        this.awsKmsClient = awsKmsClient;
        this.key = key;
    }

    public KMSEncryptionClient(Regions region, String key) {
        this(AWSKMSClientBuilder.standard().withRegion(region.getName()).build(), key);
    }

    @Override
    public String encrypt(String raw) throws AWSKMSException {
        ByteBuffer rawByteBuffer = ByteBuffer.wrap(raw.getBytes());
        EncryptRequest ereq = new EncryptRequest().withKeyId(key).withPlaintext(rawByteBuffer);
        ByteBuffer ciphertext = awsKmsClient.encrypt(ereq).getCiphertextBlob();
        return Base64.getEncoder().encodeToString(ciphertext.array());
    }

    @Override
    public String decrypt(String base64Enc) throws AWSKMSException {
        byte[] decodeBase64src = Base64.getDecoder().decode(base64Enc);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decodeBase64src);
        DecryptRequest dreq = new DecryptRequest().withCiphertextBlob(byteBuffer);
        ByteBuffer biteBufferPlainText = awsKmsClient.decrypt(dreq).getPlaintext();
        return new String(biteBufferPlainText.array());
    }
}