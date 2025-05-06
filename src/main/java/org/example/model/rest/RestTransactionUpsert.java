package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestTransactionUpsert(@JsonProperty("id") String id,
                                    @JsonProperty("orgUnitId") String orgUnitId,
                                    @JsonProperty("userId") String userId,
                                    @JsonProperty(required = true, value = "amount") double amount,
                                    @JsonProperty(required = true, value = "title") String title,
                                    @JsonProperty("createdAt") String createdAt,
                                    @JsonProperty("isLocked") boolean isLocked) {
}