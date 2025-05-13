package org.example.usecase.implementation;

import org.example.converter.OrgUnitD2BConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;
import org.example.usecase.RetrieveOrganizationOrgUnitsUseCase;
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
public class RetrieveOrganizationOrgUnitsInteractorTest {

    @Mock
    private OrgUnitGateway orgUnitGateway;

    @Mock
    private OrgUnitD2BConverter orgUnitD2BConverter;

    private RetrieveOrganizationOrgUnitsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrganizationOrgUnitsInteractor(orgUnitGateway, orgUnitD2BConverter);
    }

    @Test
    void executeShouldReturnBoundaryOrgUnits() {
        // Prepare test data for OrgUnit
        UUID orgId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        String title = "Test Org Unit";
        Optional<String> code = Optional.of("TEST001");
        Optional<String> address = Optional.of("123 Test Street");

        OrgUnit domainOrgUnit = new OrgUnit(id, orgId, title, code, address);
        List<OrgUnit> domainOrgUnits = List.of(domainOrgUnit);

        // Prepare expected BoundaryOrgUnit
        BoundaryOrgUnit boundaryOrgUnit = new BoundaryOrgUnit(id, orgId, title, code, address);
        List<BoundaryOrgUnit> boundaryOrgUnits = List.of(boundaryOrgUnit);

        // Mock behavior
        when(orgUnitGateway.retrieveByOrgId(orgId)).thenReturn(domainOrgUnits);
        when(orgUnitD2BConverter.process(domainOrgUnits)).thenReturn(boundaryOrgUnits);

        // Execute the use case
        List<BoundaryOrgUnit> result = interactor.execute(orgId).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryOrgUnits);

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieveByOrgId(orgId);
        verify(orgUnitD2BConverter).process(domainOrgUnits);
    }

    @Test
    void executeShouldHandleEmptyOrgUnits() {
        // Prepare empty test data
        List<OrgUnit> domainOrgUnits = List.of();
        List<BoundaryOrgUnit> boundaryOrgUnits = List.of();

        // Mock behavior
        when(orgUnitGateway.retrieveByOrgId(any(UUID.class))).thenReturn(domainOrgUnits);
        when(orgUnitD2BConverter.process(domainOrgUnits)).thenReturn(boundaryOrgUnits);

        // Execute the use case
        List<BoundaryOrgUnit> result = interactor.execute(UUID.randomUUID()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(orgUnitGateway).retrieveByOrgId(any(UUID.class));
        verify(orgUnitD2BConverter).process(domainOrgUnits);
    }
}
