package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationD2BConverter;
import org.example.gateway.InvitationGateway;
import org.example.model.boundary.BoundaryInvitation;
import org.example.usecase.RetrieveReceivedInvitationsUseCase;

import java.util.List;
import java.util.UUID;

public class RetrieveReceivedInvitationsInteractor implements RetrieveReceivedInvitationsUseCase {
    private final InvitationGateway invitationGateway;
    private final InvitationD2BConverter invitationD2BConverter;

    public RetrieveReceivedInvitationsInteractor(InvitationGateway invitationGateway, InvitationD2BConverter invitationD2BConverter) {
        this.invitationGateway = invitationGateway;
        this.invitationD2BConverter = invitationD2BConverter;
    }

    @Override
    public Single<List<BoundaryInvitation>> execute(UUID userId) {
        return Single.fromCallable(() -> invitationD2BConverter.process(invitationGateway.retrieveReceivedInvitations(userId)));
    }
}
