package org.example.model.boundary;

import java.util.Optional;
import java.util.UUID;

public record BoundaryOrganization(UUID id,
                           String title,
                           Optional<String> code,
                           Optional<String> address) {

}
