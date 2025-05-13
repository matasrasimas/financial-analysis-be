package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;
import org.example.usecase.RetrieveOrgUnitByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class RetrieveOrgUnitByIdInteractorTest {

    @Mock
    private OrgUnitGateway orgUnitGateway;

    @Mock
    private OrgUnitD2BConverter orgUnitD2BConverter;

    private RetrieveOrgUnitByIdUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrgUnitByIdInteractor(orgUnitGateway, orgUnitD2BConverter);
    }

    @Test
    void executeShouldReturnConvertedOrgUnitWhenFound() {
        // Prepare test data for OrgUnit (domain model)
        UUID orgUnitId = UUID.randomUUID();
        OrgUnit orgUnit = new OrgUnit(orgUnitId, UUID.randomUUID(), "Test Org Unit", Optional.of("Code 123"), Optional.of("Address 1"));

        // Prepare expected BoundaryOrgUnit (boundary model)
        BoundaryOrgUnit boundaryOrgUnit = new BoundaryOrgUnit(orgUnit.id(), orgUnit.orgId(), orgUnit.title(), orgUnit.code(), orgUnit.address());

        // Mock behavior
        when(orgUnitGateway.retrieveById(orgUnitId)).thenReturn(Optional.of(orgUnit));
        when(orgUnitD2BConverter.process(orgUnit)).thenReturn(Optional.of(boundaryOrgUnit));

        // Execute the use case
        BoundaryOrgUnit result = interactor.execute(orgUnitId).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryOrgUnit);

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieveById(orgUnitId);
        verify(orgUnitD2BConverter).process(orgUnit);
    }

    @Test
    void executeShouldThrowItemNotFoundExceptionWhenOrgUnitNotFound() {
        // Prepare test data with a non-existent OrgUnit
        UUID orgUnitId = UUID.randomUUID();

        // Mock behavior
        when(orgUnitGateway.retrieveById(orgUnitId)).thenReturn(Optional.empty());

        // Execute the use case and assert that the exception is thrown
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> interactor.execute(orgUnitId).blockingGet())
                .withMessageContaining("orgUnit with id [" + orgUnitId + "] not found");

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieveById(orgUnitId);
        verifyNoInteractions(orgUnitD2BConverter);
    }
}
