package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestAccessToken {
    private final String token;

    public RestAccessToken(String token) {
        this.token = token;
    }

    @JsonProperty("accessToken")
    public String getToken() {
        return token;
    }
}
