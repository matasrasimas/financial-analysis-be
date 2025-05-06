package org.example.converter;

import org.example.model.boundary.BoundaryInvitation;
import org.example.model.domain.Invitation;

import java.util.UUID;

public class InvitationB2DConverter extends Converter<BoundaryInvitation, Invitation> {
    @Override
    protected Invitation convert(BoundaryInvitation input) {
        return new Invitation(
                UUID.fromString(input.id()),
                UUID.fromString(input.senderId()),
                UUID.fromString(input.receiverId()),
                UUID.fromString(input.organizationId()),
                input.createdAt(),
                input.isAccepted()
        );
    }
}
