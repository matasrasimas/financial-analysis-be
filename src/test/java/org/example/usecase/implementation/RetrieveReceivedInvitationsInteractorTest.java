package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationD2BConverter;
import org.example.gateway.InvitationGateway;
import org.example.model.boundary.BoundaryInvitation;
import org.example.model.domain.Invitation;
import org.example.usecase.RetrieveReceivedInvitationsUseCase;
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
public class RetrieveReceivedInvitationsInteractorTest {

    @Mock
    private InvitationGateway invitationGateway;

    @Mock
    private InvitationD2BConverter invitationD2BConverter;

    private RetrieveReceivedInvitationsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveReceivedInvitationsInteractor(invitationGateway, invitationD2BConverter);
    }

    @Test
    void executeShouldReturnReceivedInvitationsWhenFound() {
        // Prepare test data for Invitation (domain model)
        Invitation invitation1 = new Invitation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.of(2025, 5, 10), true);
        Invitation invitation2 = new Invitation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.of(2025, 5, 12), false);

        List<Invitation> invitations = List.of(invitation1, invitation2);

        // Prepare expected BoundaryInvitation (boundary model)
        BoundaryInvitation boundaryInvitation1 = new BoundaryInvitation(invitation1.id().toString(), invitation1.senderId().toString(), invitation1.receiverId().toString(), invitation1.organizationId().toString(), invitation1.createdAt(), invitation1.isAccepted());
        BoundaryInvitation boundaryInvitation2 = new BoundaryInvitation(invitation2.id().toString(), invitation2.senderId().toString(), invitation2.receiverId().toString(), invitation2.organizationId().toString(), invitation2.createdAt(), invitation2.isAccepted());

        List<BoundaryInvitation> boundaryInvitations = List.of(boundaryInvitation1, boundaryInvitation2);

        // Mock behavior
        when(invitationGateway.retrieveReceivedInvitations(any())).thenReturn(invitations);
        when(invitationD2BConverter.process(invitations)).thenReturn(boundaryInvitations);

        // Execute the use case
        List<BoundaryInvitation> result = interactor.execute(UUID.randomUUID()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryInvitations);

        // Verify interactions with mocks
        verify(invitationGateway).retrieveReceivedInvitations(any());
        verify(invitationD2BConverter).process(invitations);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoInvitationsFound() {
        // Prepare test data for an empty list of invitations
        List<Invitation> invitations = List.of();
        List<BoundaryInvitation> boundaryInvitations = List.of();

        // Mock behavior
        when(invitationGateway.retrieveReceivedInvitations(any())).thenReturn(invitations);
        when(invitationD2BConverter.process(invitations)).thenReturn(boundaryInvitations);

        // Execute the use case
        List<BoundaryInvitation> result = interactor.execute(UUID.randomUUID()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryInvitations);

        // Verify interactions with mocks
        verify(invitationGateway).retrieveReceivedInvitations(any());
        verify(invitationD2BConverter).process(invitations);
    }

    @Test
    void executeShouldReturnConvertedInvitations() {
        // Prepare test data for Invitation (domain model)
        Invitation invitation1 = new Invitation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.of(2025, 5, 10), true);

        List<Invitation> invitations = List.of(invitation1);

        // Prepare expected BoundaryInvitation (boundary model)
        BoundaryInvitation boundaryInvitation1 = new BoundaryInvitation(invitation1.id().toString(), invitation1.senderId().toString(), invitation1.receiverId().toString(), invitation1.organizationId().toString(), invitation1.createdAt(), invitation1.isAccepted());

        List<BoundaryInvitation> boundaryInvitations = List.of(boundaryInvitation1);

        // Mock behavior
        when(invitationGateway.retrieveReceivedInvitations(any())).thenReturn(invitations);
        when(invitationD2BConverter.process(invitations)).thenReturn(boundaryInvitations);

        // Execute the use case
        List<BoundaryInvitation> result = interactor.execute(UUID.randomUUID()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions - check if the invitation is converted correctly
        assertThat(result.get(0).id()).isEqualTo(boundaryInvitation1.id());
        assertThat(result.get(0).senderId()).isEqualTo(boundaryInvitation1.senderId());
        assertThat(result.get(0).receiverId()).isEqualTo(boundaryInvitation1.receiverId());

        // Verify interactions with mocks
        verify(invitationGateway).retrieveReceivedInvitations(any());
        verify(invitationD2BConverter).process(invitations);
    }
}
