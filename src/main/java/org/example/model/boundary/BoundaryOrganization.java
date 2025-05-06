package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryOrganization(UUID id,
                                   UUID userId,
                                   String title,
                                   Optional<String> code,
                                   Optional<String> address,
                                   double yearlyGoal) {
}
