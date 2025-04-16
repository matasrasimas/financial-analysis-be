package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record AutomaticTransaction(UUID id,
                                   UUID orgUnitId,
                                   double amount,
                                   String title,
                                   Optional<String> description,
                                   int duration,
                                   DurationUnit durationUnit) {

    public enum DurationUnit {
        MINUTES,
        HOURS,
        DAYS
    }
}
