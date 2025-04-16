package org.example.model.domain;

import java.util.Optional;

public record OrganizationCreate(String title,
                                 Optional<String> code,
                                 Optional<String> address) {
}
