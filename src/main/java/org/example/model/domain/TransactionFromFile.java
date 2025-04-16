package org.example.model.domain;

import java.time.LocalDate;
import java.util.Optional;

public record TransactionFromFile(Optional<Float> amount,
                                  Optional<String> title,
                                  Optional<String> description,
                                  Optional<LocalDate> createdAt) {
}
