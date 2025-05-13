package org.example.usecase.implementation;

import org.example.gateway.InvitationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteInvitationInteractorTest {

    @Mock
    private InvitationGateway invitationGateway;

    private DeleteInvitationInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteInvitationInteractor(invitationGateway);
    }

    @Test
    void executeSuccessfullyDeletesInvitation() {
        UUID invitationId = UUID.randomUUID();

        doNothing().when(invitationGateway).deleteInvitation(invitationId);

        interactor.execute(invitationId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(invitationGateway).deleteInvitation(invitationId);
    }

    @Test
    void executePropagatesException() {
        UUID invitationId = UUID.randomUUID();
        RuntimeException exception = new RuntimeException("Deletion failed");

        doThrow(exception).when(invitationGateway).deleteInvitation(invitationId);

        interactor.execute(invitationId)
                .test()
                .assertError(exception);

        verify(invitationGateway).deleteInvitation(invitationId);
    }
}
