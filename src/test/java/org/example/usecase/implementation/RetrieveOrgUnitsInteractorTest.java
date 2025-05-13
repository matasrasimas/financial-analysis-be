package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;
import org.example.usecase.RetrieveOrgUnitsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RetrieveOrgUnitsInteractorTest {

    @Mock
    private OrgUnitGateway orgUnitGateway;

    @Mock
    private OrgUnitD2BConverter orgUnitD2BConverter;

    private RetrieveOrgUnitsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrgUnitsInteractor(orgUnitGateway, orgUnitD2BConverter);
    }

    @Test
    void executeShouldReturnConvertedOrgUnitsWhenFound() {
        // Prepare test data for OrgUnit (domain model)
        OrgUnit orgUnit1 = new OrgUnit(UUID.randomUUID(), UUID.randomUUID(), "Org Unit 1", Optional.of("Code 1"), Optional.of("Address 1"));
        OrgUnit orgUnit2 = new OrgUnit(UUID.randomUUID(), UUID.randomUUID(), "Org Unit 2", Optional.of("Code 2"), Optional.of("Address 2"));
        List<OrgUnit> orgUnits = List.of(orgUnit1, orgUnit2);

        // Prepare expected BoundaryOrgUnit (boundary model)
        BoundaryOrgUnit boundaryOrgUnit1 = new BoundaryOrgUnit(orgUnit1.id(), orgUnit1.orgId(), orgUnit1.title(), orgUnit1.code(), orgUnit1.address());
        BoundaryOrgUnit boundaryOrgUnit2 = new BoundaryOrgUnit(orgUnit2.id(), orgUnit2.orgId(), orgUnit2.title(), orgUnit2.code(), orgUnit2.address());
        List<BoundaryOrgUnit> boundaryOrgUnits = List.of(boundaryOrgUnit1, boundaryOrgUnit2);

        // Mock behavior
        when(orgUnitGateway.retrieve()).thenReturn(orgUnits);
        when(orgUnitD2BConverter.process(orgUnits)).thenReturn(boundaryOrgUnits);

        // Execute the use case
        List<BoundaryOrgUnit> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryOrgUnits);

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieve();
        verify(orgUnitD2BConverter).process(orgUnits);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoOrgUnitsFound() {
        // Prepare test data for an empty OrgUnit list
        List<OrgUnit> orgUnits = List.of();

        // Prepare expected empty list for BoundaryOrgUnits
        List<BoundaryOrgUnit> boundaryOrgUnits = List.of();

        // Mock behavior
        when(orgUnitGateway.retrieve()).thenReturn(orgUnits);
        when(orgUnitD2BConverter.process(orgUnits)).thenReturn(boundaryOrgUnits);

        // Execute the use case
        List<BoundaryOrgUnit> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryOrgUnits);

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieve();
        verify(orgUnitD2BConverter).process(orgUnits);
    }
}
