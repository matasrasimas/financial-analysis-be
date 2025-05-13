package org.example.usecase.implementation;

import org.example.gateway.OrgUnitGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteOrgUnitInteractorTest {

    @Mock
    private OrgUnitGateway orgUnitGateway;

    private DeleteOrgUnitInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteOrgUnitInteractor(orgUnitGateway);
    }

    @Test
    void executeSuccessfullyDeletesOrgUnit() {
        UUID orgUnitId = UUID.randomUUID();
        doNothing().when(orgUnitGateway).delete(orgUnitId);

        interactor.execute(orgUnitId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(orgUnitGateway).delete(orgUnitId);
    }

    @Test
    void executePropagatesExceptionOnFailure() {
        UUID orgUnitId = UUID.randomUUID();
        RuntimeException exception = new RuntimeException("Delete failed");

        doThrow(exception).when(orgUnitGateway).delete(orgUnitId);

        interactor.execute(orgUnitId)
                .test()
                .assertError(exception);

        verify(orgUnitGateway).delete(orgUnitId);
    }
}
