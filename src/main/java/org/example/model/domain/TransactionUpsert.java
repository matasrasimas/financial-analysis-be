package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record TransactionUpsert(UUID id,
                                double amount,
                                String title,
                                Optional<String> description) {
}
