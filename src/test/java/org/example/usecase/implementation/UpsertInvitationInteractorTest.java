package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationB2DConverter;
import org.example.converter.InvitationD2BConverter;
import org.example.gateway.InvitationGateway;
import org.example.model.boundary.BoundaryInvitation;
import org.example.model.domain.Invitation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UpsertInvitationInteractorTest {

    private InvitationGateway invitationGateway;
    private InvitationB2DConverter b2dConverter;
    private InvitationD2BConverter d2bConverter;

    private UpsertInvitationInteractor interactor;

    @BeforeEach
    void setUp() {
        invitationGateway = mock(InvitationGateway.class);
        b2dConverter = mock(InvitationB2DConverter.class);
        d2bConverter = mock(InvitationD2BConverter.class);

        interactor = new UpsertInvitationInteractor(invitationGateway, b2dConverter, d2bConverter);
    }

    @Test
    void execute_shouldConvertUpsertAndReturnResult() {
        // Arrange
        BoundaryInvitation boundaryInput = new BoundaryInvitation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                LocalDate.now(),
                false
        );

        Invitation domainInvitation = new Invitation(
                UUID.fromString(boundaryInput.id()),
                UUID.fromString(boundaryInput.senderId()),
                UUID.fromString(boundaryInput.receiverId()),
                UUID.fromString(boundaryInput.organizationId()),
                boundaryInput.createdAt(),
                boundaryInput.isAccepted()
        );

        BoundaryInvitation boundaryOutput = boundaryInput;

        when(b2dConverter.process(boundaryInput)).thenReturn(Optional.of(domainInvitation));
        when(invitationGateway.upsertInvitation(domainInvitation)).thenReturn(domainInvitation);
        when(d2bConverter.process(domainInvitation)).thenReturn(Optional.of(boundaryOutput));

        // Act
        Single<BoundaryInvitation> result = interactor.execute(boundaryInput);

        // Assert
        BoundaryInvitation actual = result.blockingGet();
        assertThat(actual).isEqualTo(boundaryOutput);

        verify(b2dConverter).process(boundaryInput);
        verify(invitationGateway).upsertInvitation(domainInvitation);
        verify(d2bConverter).process(domainInvitation);
    }
}
