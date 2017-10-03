package com.tn.dw;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

class TestConfig extends Configuration {

    @NotNull
    @JsonProperty
    private Foo foo;

    @NotNull
    @JsonProperty
    private Bar bar;

    public Foo getFoo() {
        return foo;
    }

    public Bar getBar() {
        return bar;
    }

    public static class Foo {
        @NotNull
        @JsonProperty
        private String token;

        public String getToken() {
            return token;
        }
    }

    public static class Bar {
        @NotNull
        @JsonProperty
        private AuthConfig auth;

        public AuthConfig getAuth() {
            return auth;
        }
    }

    public static class AuthConfig {
        @NotNull
        @JsonProperty
        private String url;

        @NotNull
        @JsonProperty
        private String key;

        @NotNull
        @JsonProperty
        private String secret;

        @JsonProperty
        private UserToken[] tokens;

        public String getUrl() {
            return url;
        }

        public String getKey() {
            return key;
        }

        public String getSecret() {
            return secret;
        }

        public UserToken[] getTokens() {
            return tokens;
        }
    }

    public static class UserToken {
        @NotNull
        @JsonProperty
        private String key;

        @NotNull
        @JsonProperty
        private String secret;

        public String getKey() {
            return key;
        }

        public String getSecret() {
            return secret;
        }
    }
}
