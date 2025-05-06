package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryAutomaticTransaction(UUID id,
                                           UUID orgUnitId,
                                           double amount,
                                           String title,
                                           Optional<String> description,
                                           int duration,
                                           String durationUnit,
                                           String nextTransactionDate) {
}
