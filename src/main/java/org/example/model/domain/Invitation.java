package org.example.model.domain;

import java.time.LocalDate;
import java.util.UUID;

public record Invitation(UUID id,
                         UUID senderId,
                         UUID receiverId,
                         UUID organizationId,
                         LocalDate createdAt,
                         boolean isAccepted) {
}
