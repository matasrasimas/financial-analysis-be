package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record RestTransaction(@JsonProperty(required = true, value = "id") String id,
                              @JsonProperty(required = true, value = "amount") double amount,
                              @JsonProperty(required = true, value = "title") String title,
                              @JsonProperty("description") String description,
                              @JsonProperty(required = true, value = "createdAt") Instant createdAt) {
}
