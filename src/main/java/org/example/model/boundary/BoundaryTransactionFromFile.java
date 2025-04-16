package org.example.model.boundary;

import java.time.LocalDate;
import java.util.Optional;

public record BoundaryTransactionFromFile(Optional<Float> amount,
                                          Optional<String> title,
                                          Optional<String> description,
                                          Optional<LocalDate> createdAt) {
}
