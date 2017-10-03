package com.tn.dw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.dropwizard.jackson.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigDecrypterTest {
    private TestConfig testConfig;

    @Before
    public void setUp() throws Exception {
        String s = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("config-test.yaml"), "UTF-8");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModules(new GuavaModule(), new LogbackModule(), new GuavaExtrasModule(), new JodaModule(), new AfterburnerModule(), new FuzzyEnumModule(), new Jdk8Module(), new JavaTimeModule());
        mapper.setPropertyNamingStrategy(new AnnotationSensitivePropertyNamingStrategy());
        mapper.setSubtypeResolver(new DiscoverableSubtypeResolver());
        testConfig = mapper.readValue(s.getBytes(), TestConfig.class);
    }

    @Test
    public void testDecrypt() throws Exception {

        ConfigDecrypter decrypter = new ConfigDecrypter(new EncryptionClient() {
            @Override
            public String encrypt(String raw) {
                throw new RuntimeException("Should not be called in this test");
            }

            @Override
            public String decrypt(String base64Enc) {
                return "decrypted:" + base64Enc;
            }
        });
        decrypter.decrypt(testConfig);

        assertEquals("decrypted:fooToken", testConfig.getFoo().getToken());

        TestConfig.AuthConfig auth = testConfig.getBar().getAuth();
        assertEquals("decrypted:barSecret", auth.getSecret());
        assertEquals("barToken1", auth.getTokens()[0].getKey());
        assertEquals("decrypted:barTokenSecret1", auth.getTokens()[0].getSecret());
        assertEquals("barToken2", auth.getTokens()[1].getKey());
        assertEquals("decrypted:barTokenSecret2", auth.getTokens()[1].getSecret());
    }
}