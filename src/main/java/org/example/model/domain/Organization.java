package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record Organization(UUID id,
                           UUID userId,
                           String title,
                           Optional<String> code,
                           Optional<String> address) {
}
