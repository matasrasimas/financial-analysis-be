package org.example.model.domain;

import java.util.Optional;
import java.util.UUID;

public record OrgUnit(UUID id,
                      UUID orgId,
                      String title,
                      Optional<String> code,
                      Optional<String> address) {
}
