package org.example.model.boundary;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record BoundaryTransaction(UUID id,
                                  UUID orgUnitId,
                                  double amount,
                                  String title,
                                  Optional<String> description,
                                  LocalDate createdAt) {
}
