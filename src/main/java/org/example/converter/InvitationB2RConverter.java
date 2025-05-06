package org.example.converter;

import org.example.model.boundary.BoundaryInvitation;
import org.example.model.rest.RestInvitation;

public class InvitationB2RConverter extends Converter<BoundaryInvitation, RestInvitation> {
    @Override
    protected RestInvitation convert(BoundaryInvitation input) {
        return new RestInvitation(
                input.id(),
                input.senderId(),
                input.receiverId(),
                input.organizationId(),
                input.createdAt().toString(),
                input.isAccepted()
        );
    }
}
