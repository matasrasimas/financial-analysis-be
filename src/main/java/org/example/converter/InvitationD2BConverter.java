package org.example.converter;

import org.example.model.boundary.BoundaryInvitation;
import org.example.model.domain.Invitation;

public class InvitationD2BConverter extends Converter<Invitation, BoundaryInvitation> {
    @Override
    protected BoundaryInvitation convert(Invitation input) {
        return new BoundaryInvitation(
                input.id().toString(),
                input.senderId().toString(),
                input.receiverId().toString(),
                input.organizationId().toString(),
                input.createdAt(),
                input.isAccepted()
        );
    }
}
