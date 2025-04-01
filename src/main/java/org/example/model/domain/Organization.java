package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record Organization(UUID id,
                           String title,
                           Optional<String> code,
                           Optional<String> address) {

}
