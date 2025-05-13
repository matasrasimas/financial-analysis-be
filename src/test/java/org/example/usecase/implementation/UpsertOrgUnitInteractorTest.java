package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrgUnitB2DConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UpsertOrgUnitInteractorTest {

    private OrgUnitGateway orgUnitGateway;
    private OrgUnitB2DConverter orgUnitB2DConverter;

    private UpsertOrgUnitInteractor interactor;

    @BeforeEach
    void setUp() {
        orgUnitGateway = mock(OrgUnitGateway.class);
        orgUnitB2DConverter = mock(OrgUnitB2DConverter.class);
        interactor = new UpsertOrgUnitInteractor(orgUnitGateway, orgUnitB2DConverter);
    }

    @Test
    void execute_shouldConvertAndUpsertSuccessfully() {
        // Arrange
        BoundaryOrgUnit boundaryInput = new BoundaryOrgUnit(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Finance",
                Optional.of("FIN001"),
                Optional.of("New York")
        );

        OrgUnit domainOrgUnit = new OrgUnit(
                boundaryInput.id(),
                boundaryInput.orgId(),
                boundaryInput.title(),
                boundaryInput.code(),
                boundaryInput.address()
        );

        when(orgUnitB2DConverter.process(boundaryInput)).thenReturn(Optional.of(domainOrgUnit));

        // Act
        Completable result = interactor.execute(boundaryInput);

        // Assert
        result.test()
                .assertComplete()
                .assertNoErrors();

        verify(orgUnitB2DConverter).process(boundaryInput);
        verify(orgUnitGateway).upsert(domainOrgUnit);
    }
}
