package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationD2BConverter;
import org.example.gateway.InvitationGateway;
import org.example.model.boundary.BoundaryInvitation;
import org.example.model.domain.Invitation;
import org.example.usecase.RetrieveInvitationsByOrgIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RetrieveInvitationsByOrgIdInteractorTest {

    @Mock
    private InvitationGateway invitationGateway;

    @Mock
    private InvitationD2BConverter invitationD2BConverter;

    private RetrieveInvitationsByOrgIdUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveInvitationsByOrgIdInteractor(invitationGateway, invitationD2BConverter);
    }

    @Test
    void executeShouldReturnBoundaryInvitations() {
        // Prepare test data for Invitation
        UUID orgId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        LocalDate createdAt = LocalDate.of(2025, 5, 13);
        boolean isAccepted = true;

        Invitation domainInvitation = new Invitation(id, senderId, receiverId, orgId, createdAt, isAccepted);
        List<Invitation> domainInvitations = List.of(domainInvitation);

        // Prepare expected BoundaryInvitation
        String strId = id.toString();
        String strSenderId = senderId.toString();
        String strReceiverId = receiverId.toString();
        String strOrgId = orgId.toString();
        BoundaryInvitation boundaryInvitation = new BoundaryInvitation(strId, strSenderId, strReceiverId, strOrgId, createdAt, isAccepted);
        List<BoundaryInvitation> boundaryInvitations = List.of(boundaryInvitation);

        // Mock behavior
        when(invitationGateway.retrieveByOrgId(orgId)).thenReturn(domainInvitations);
        when(invitationD2BConverter.process(domainInvitations)).thenReturn(boundaryInvitations);

        // Execute the use case
        List<BoundaryInvitation> result = interactor.execute(orgId).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryInvitations);

        // Verify interactions with mocks
        verify(invitationGateway).retrieveByOrgId(orgId);
        verify(invitationD2BConverter).process(domainInvitations);
    }

    @Test
    void executeShouldHandleEmptyInvitations() {
        // Prepare empty test data
        List<Invitation> domainInvitations = List.of();
        List<BoundaryInvitation> boundaryInvitations = List.of();

        // Mock behavior
        when(invitationGateway.retrieveByOrgId(any(UUID.class))).thenReturn(domainInvitations);
        when(invitationD2BConverter.process(domainInvitations)).thenReturn(boundaryInvitations);

        // Execute the use case
        List<BoundaryInvitation> result = interactor.execute(UUID.randomUUID()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(invitationGateway).retrieveByOrgId(any(UUID.class));
        verify(invitationD2BConverter).process(domainInvitations);
    }
}
