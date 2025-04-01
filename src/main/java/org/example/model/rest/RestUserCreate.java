package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestUserCreate(@JsonProperty(required = true, value = "firstName") String firstName,
                             @JsonProperty(required = true, value = "lastName") String lastName,
                             @JsonProperty("phoneNumber") String phoneNumber,
                             @JsonProperty("email") String email,
                             @JsonProperty( required = true, value = "password") String password) {
}
