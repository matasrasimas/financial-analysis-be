package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record UserDTO(UUID id,
                      String firstName,
                      String lastName,
                      Optional<String> phoneNumber,
                      Optional<String> email) {
}
