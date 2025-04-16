package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestUserDTO(@JsonProperty(required = true, value = "id") String id,
                          @JsonProperty(required = true, value = "firstName") String firstName,
                          @JsonProperty(required = true, value = "lastName") String lastName,
                          @JsonProperty("phoneNumber") String phoneNumber,
                          @JsonProperty(required = true, value = "email") String email) {
}