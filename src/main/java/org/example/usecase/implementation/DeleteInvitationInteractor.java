package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.gateway.InvitationGateway;
import org.example.usecase.DeleteInvitationUseCase;

import java.util.UUID;

public class DeleteInvitationInteractor implements DeleteInvitationUseCase {
    private final InvitationGateway invitationGateway;

    public DeleteInvitationInteractor(InvitationGateway invitationGateway) {
        this.invitationGateway = invitationGateway;
    }

    @Override
    public Completable execute(UUID invitationId) {
        return Completable.fromAction(() -> invitationGateway.deleteInvitation(invitationId));
    }
}
