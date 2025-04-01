package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryUserDTO(UUID id,
                              String firstName,
                              String lastName,
                              Optional<String> phoneNumber,
                              Optional<String> email) {
}
