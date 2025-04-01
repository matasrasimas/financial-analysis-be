package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record UserCreate(String firstName,
                         String lastName,
                         Optional<String> phoneNumber,
                         Optional<String> email,
                         String password) {
}
