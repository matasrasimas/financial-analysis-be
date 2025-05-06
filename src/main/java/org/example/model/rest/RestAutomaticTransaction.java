package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestAutomaticTransaction(@JsonProperty("id") String id,
                                       @JsonProperty(required = true, value = "orgUnitId") String orgUnitId,
                                       @JsonProperty(required = true, value = "amount") double amount,
                                       @JsonProperty(required = true, value = "title") String title,
                                       @JsonProperty("description") String description,
                                       @JsonProperty(required = true, value = "duration") int duration,
                                       @JsonProperty(required = true, value = "durationUnit") String durationUnit,
                                       @JsonProperty("nextTransactionDate") String nextTransactionDate) {
}
