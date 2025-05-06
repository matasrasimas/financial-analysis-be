package org.example.model.boundary;

import java.time.LocalDate;

public record BoundaryInvitation(String id,
                                 String senderId,
                                 String receiverId,
                                 String organizationId,
                                 LocalDate createdAt,
                                 boolean isAccepted) {
}
