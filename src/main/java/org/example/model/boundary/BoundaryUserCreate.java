package org.example.model.boundary;

import java.util.Optional;

public record BoundaryUserCreate(String firstName,
                                 String lastName,
                                 Optional<String> phoneNumber,
                                 Optional<String> email,
                                 String password) {
}
