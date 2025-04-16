package org.example.model.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record TransactionUpsert(UUID id,
                                UUID orgUnitId,
                                double amount,
                                String title,
                                Optional<String> description,
                                LocalDate createdAt) {
}
