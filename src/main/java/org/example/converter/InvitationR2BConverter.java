package org.example.converter;

import org.example.model.boundary.BoundaryInvitation;
import org.example.model.rest.RestInvitation;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class InvitationR2BConverter extends Converter<RestInvitation, BoundaryInvitation> {
    @Override
    protected BoundaryInvitation convert(RestInvitation input) {
        return new BoundaryInvitation(
                Optional.ofNullable(input.id()).orElse(UUID.randomUUID().toString()),
                input.senderId(),
                input.receiverId(),
                input.organizationId(),
                LocalDate.parse(input.createdAt()),
                input.isAccepted()
        );
    }
}
