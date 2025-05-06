package org.example.model.boundary;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record BoundaryTransactionUpsert(UUID id,
                                        UUID orgUnitId,
                                        UUID userId,
                                        double amount,
                                        String title,
                                        LocalDate createdAt,
                                        boolean isLocked) {
}
