package org.example.model.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record AutomaticTransaction(UUID id,
                                   double amount,
                                   String title,
                                   Optional<String> description,
                                   Instant latestTransactionDate,
                                   int durationMinutes,
                                   DurationUnit durationUnit) {

    public enum DurationUnit {
        MINUTES,
        HOURS,
        DAYS
    }
}
