package com.tn.dw;

import com.tn.dw.util.ThrowingConsumer;
import io.dropwizard.Configuration;
import io.dropwizard.jackson.Discoverable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scans the given object and its children for String fields that match the pattern ENC[enc-val], and attempts to decrypt them.
 *
 * @author tnagelkerke
 */
public class ConfigDecrypter {
    private final EncryptionClient encryptionClient;

    public ConfigDecrypter(EncryptionClient encryptionClient) {
        this.encryptionClient = encryptionClient;
    }

    public void decrypt(Object config) {
        String scanPackage = getPackage(config);

        // get all fields in the (super)class hierarchy
        List<Class<?>> classHierarchy = getClassHierarchy(config);
        List<Field> fields = classHierarchy.stream().flatMap(clazz -> Stream.of(clazz.getDeclaredFields())).collect(Collectors.toList());

        fields.forEach((ThrowingConsumer<Field>) field -> {
            field.setAccessible(true);
            Object o = field.get(config);
            if (o instanceof String) {
                String original = (String) o;
                Pattern pattern = Pattern.compile("ENC\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(original);

                if (matcher.find()) {
                    String decrypted = encryptionClient.decrypt(matcher.group(1));
                    field.set(config, decrypted);
                }
            } else {
                if (isConfigBean(o, scanPackage)) { // object
                    decrypt(o);
                } else if (o != null && o instanceof Object[]) { // array
                    Stream.of((Object[]) o).forEach(element -> {
                        if (isConfigBean(element, scanPackage)) {
                            decrypt(element);
                        }
                    });
                } else if (o != null && o instanceof Collection<?>) { // collection
                    ((Collection<?>) o).forEach(element -> {
                        if (isConfigBean(element, scanPackage)) {
                            decrypt(element);
                        }
                    });
                }
            }
        });
    }

    private static boolean isConfigBean(Object o, String scanPackage) {
        return o != null && !o.getClass().isEnum()
                && (getPackage(o).startsWith(scanPackage) || getPackage(o).startsWith("io.dropwizard") || o instanceof Configuration || o instanceof Discoverable);
    }

    private static String getPackage(Object o) {
        return o != null && o.getClass().getPackage() != null ? o.getClass().getPackage().getName() : "";
    }

    private static List<Class<?>> getClassHierarchy(Object config) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> clazz = config.getClass();
        classes.add(clazz);
        while (clazz.getSuperclass() != null) {
            classes.add(clazz.getSuperclass());
            clazz = clazz.getSuperclass();
        }
        return classes;
    }
}
