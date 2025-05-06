package org.example.gateway;

import org.example.model.domain.Invitation;

import java.util.List;
import java.util.UUID;

public interface InvitationGateway {
    List<Invitation> retrieveSentInvitations(UUID senderId);
    List<Invitation> retrieveReceivedInvitations(UUID receiverId);
    List<Invitation> retrieveByOrgId(UUID orgId);
    Invitation upsertInvitation(Invitation input);
    void deleteInvitation(UUID id);
}
