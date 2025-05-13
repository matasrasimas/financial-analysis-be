package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationD2BConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;
import org.example.usecase.RetrieveOrganizationsUseCase;
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
public class RetrieveOrganizationsInteractorTest {

    @Mock
    private OrganizationGateway organizationGateway;

    @Mock
    private OrganizationD2BConverter organizationD2BConverter;

    private RetrieveOrganizationsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Test
    void executeShouldReturnBoundaryOrganizations() {
        // Prepare test data for Organization (domain model)
        UUID orgId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String title = "Test Organization";
        Optional<String> code = Optional.of("ORG001");
        Optional<String> address = Optional.of("123 Test Street");
        double yearlyGoal = 100000.0;

        Organization domainOrganization = new Organization(orgId, userId, title, code, address, yearlyGoal);
        List<Organization> domainOrganizations = List.of(domainOrganization);

        // Prepare expected BoundaryOrganization (boundary model)
        BoundaryOrganization boundaryOrganization = new BoundaryOrganization(orgId, userId, title, code, address, yearlyGoal);
        List<BoundaryOrganization> boundaryOrganizations = List.of(boundaryOrganization);

        // Mock behavior
        when(organizationGateway.retrieve()).thenReturn(domainOrganizations);
        when(organizationD2BConverter.process(domainOrganizations)).thenReturn(boundaryOrganizations);

        // Execute the use case
        List<BoundaryOrganization> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryOrganizations);

        // Verify interactions with mocks
        verify(organizationGateway).retrieve();
        verify(organizationD2BConverter).process(domainOrganizations);
    }

    @Test
    void executeShouldHandleEmptyOrganizations() {
        // Prepare empty test data
        List<Organization> domainOrganizations = List.of();
        List<BoundaryOrganization> boundaryOrganizations = List.of();

        // Mock behavior
        when(organizationGateway.retrieve()).thenReturn(domainOrganizations);
        when(organizationD2BConverter.process(domainOrganizations)).thenReturn(boundaryOrganizations);

        // Execute the use case
        List<BoundaryOrganization> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(organizationGateway).retrieve();
        verify(organizationD2BConverter).process(domainOrganizations);
    }
}
