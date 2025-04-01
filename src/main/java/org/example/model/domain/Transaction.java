package org.example.model.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public record Transaction(UUID id,
                          double amount,
                          String title,
                          Optional<String> description,
                          Instant createdAt) {
}
