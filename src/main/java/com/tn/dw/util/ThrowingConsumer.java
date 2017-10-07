package com.tn.dw.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Consumer that rethrows caught exceptions as RuntimeException.
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    Logger LOG = LoggerFactory.getLogger(ThrowingConsumer.class);

    @Override
    default void accept(T elem) {
        try {
            acceptThrows(elem);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T elem) throws Exception;

}
