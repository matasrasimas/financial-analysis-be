package org.example.factory;

import org.example.usecase.*;

public interface InvitationUseCaseFactory {
    RetrieveSentInvitationsUseCase createRetrieveSentInvitationsUseCase();
    RetrieveReceivedInvitationsUseCase createRetrieveReceivedInvitationsUseCase();
    RetrieveInvitationsByOrgIdUseCase createRetrieveInvitationsByOrgIdUseCase();
    UpsertInvitationUseCase createUpsertInvitationUseCase();
    DeleteInvitationUseCase createDeleteInvitationUseCase();
}
