package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record RestTransactionFromFile(@JsonProperty("amount") Float amount,
                                      @JsonProperty("title") String title,
                                      @JsonProperty("description") String description,
                                      @JsonProperty("createdAt") LocalDate createdAt) {
}
