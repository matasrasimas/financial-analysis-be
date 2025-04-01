package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryOrgUnit(UUID id,
                              UUID orgId,
                              String title,
                              Optional<String> code,
                              Optional<String> address) {
}