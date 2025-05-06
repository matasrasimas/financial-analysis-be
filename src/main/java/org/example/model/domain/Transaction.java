package org.example.model.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record Transaction(UUID id,
                          UUID orgUnitId,
                          UUID userId,
                          double amount,
                          String title,
                          LocalDate createdAt,
                          boolean isLocked) {
}
