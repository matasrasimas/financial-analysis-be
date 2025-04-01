package org.example.model.boundary;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record BoundaryTransaction(UUID id,
                                  double amount,
                                  String title,
                                  Optional<String> description,
                                  Instant createdAt) {
}
