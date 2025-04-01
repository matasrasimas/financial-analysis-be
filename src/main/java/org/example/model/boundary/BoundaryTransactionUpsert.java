package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryTransactionUpsert(UUID id,
                                        double amount,
                                        String title,
                                        Optional<String> description) {
}
