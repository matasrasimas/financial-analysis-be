package org.example.model.boundary;

import java.util.Optional;

public record BoundaryOrganizationCreate(String title,
                                         Optional<String> code,
                                         Optional<String> address) {
}
