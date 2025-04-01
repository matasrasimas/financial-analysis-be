package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestUserLoginDTO(@JsonProperty(required = true, value = "email") String email,
                               @JsonProperty(required = true, value = "password") String password) {
}
