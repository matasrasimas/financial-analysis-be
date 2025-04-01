package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record RestAutomaticTransaction(@JsonProperty("id") String id,
                                       @JsonProperty(required = true, value = "amount") double amount,
                                       @JsonProperty(required = true, value = "title") String title,
                                       @JsonProperty("description") String description,
                                       @JsonProperty(required = true, value = "latestTransactionDate") Instant latestTransactionDate,
                                       @JsonProperty(required = true, value = "durationMinutes") int durationMinutes,
                                       @JsonProperty(required = true, value = "durationUnit") String durationUnit) {
}
