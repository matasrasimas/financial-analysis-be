package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;
import org.example.usecase.RetrieveUserOrganizationsUseCase;
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
public class RetrieveUserOrganizationsInteractorTest {

    @Mock
    private OrganizationGateway organizationGateway;

    @Mock
    private OrganizationD2BConverter organizationD2BConverter;

    private RetrieveUserOrganizationsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveUserOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Test
    void executeShouldReturnOrganizationsWhenFound() {
        // Prepare test data for Organization (domain model)
        Organization organization1 = new Organization(UUID.randomUUID(), UUID.randomUUID(), "Organization 1", Optional.of("Code1"), Optional.of("Address 1"), 100000);
        Organization organization2 = new Organization(UUID.randomUUID(), UUID.randomUUID(), "Organization 2", Optional.of("Code2"), Optional.of("Address 2"), 200000);

        List<Organization> organizations = List.of(organization1, organization2);

        // Prepare expected BoundaryOrganization (boundary model)
        BoundaryOrganization boundaryOrganization1 = new BoundaryOrganization(organization1.id(), organization1.userId(), organization1.title(), organization1.code(), organization1.address(), organization1.yearlyGoal());
        BoundaryOrganization boundaryOrganization2 = new BoundaryOrganization(organization2.id(), organization2.userId(), organization2.title(), organization2.code(), organization2.address(), organization2.yearlyGoal());

        List<BoundaryOrganization> boundaryOrganizations = List.of(boundaryOrganization1, boundaryOrganization2);

        // Mock behavior
        when(organizationGateway.retrieveByUserId(organization1.userId())).thenReturn(organizations);
        when(organizationD2BConverter.process(organizations)).thenReturn(boundaryOrganizations);

        // Execute the use case
        List<BoundaryOrganization> result = interactor.execute(organization1.userId()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryOrganizations);

        // Verify interactions with mocks
        verify(organizationGateway).retrieveByUserId(organization1.userId());
        verify(organizationD2BConverter).process(organizations);
    }

    @Test
    void executeShouldThrowItemNotFoundExceptionWhenNoOrganizationsFound() {
        // Prepare test data for an empty list of organizations
        List<Organization> organizations = List.of();

        UUID userId = UUID.randomUUID();
        // Mock behavior
        when(organizationGateway.retrieveByUserId(userId)).thenReturn(organizations);

        // Execute the use case and verify exception is thrown
        interactor.execute(userId).test()
                .assertError(e -> e.getClass().equals(ItemNotFoundException.class)
                    && e.getMessage().matches("organizations with user id .* not found"));

        // Verify interactions with mocks
        verify(organizationGateway).retrieveByUserId(any(UUID.class));
        verifyNoInteractions(organizationD2BConverter);
    }

    @Test
    void executeShouldReturnConvertedOrganizations() {
        // Prepare test data for Organization (domain model)
        Organization organization = new Organization(UUID.randomUUID(), UUID.randomUUID(), "Organization 1", Optional.of("Code1"), Optional.of("Address 1"), 100000);

        // Prepare expected BoundaryOrganization (boundary model)
        BoundaryOrganization boundaryOrganization = new BoundaryOrganization(organization.id(), organization.userId(), organization.title(), organization.code(), organization.address(), organization.yearlyGoal());

        List<Organization> organizations = List.of(organization);
        List<BoundaryOrganization> boundaryOrganizations = List.of(boundaryOrganization);

        // Mock behavior
        when(organizationGateway.retrieveByUserId(organization.userId())).thenReturn(organizations);
        when(organizationD2BConverter.process(organizations)).thenReturn(boundaryOrganizations);

        // Execute the use case
        List<BoundaryOrganization> result = interactor.execute(organization.userId()).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions - check if the organization is converted correctly
        assertThat(result.get(0).id()).isEqualTo(boundaryOrganization.id());
        assertThat(result.get(0).userId()).isEqualTo(boundaryOrganization.userId());
        assertThat(result.get(0).title()).isEqualTo(boundaryOrganization.title());

        // Verify interactions with mocks
        verify(organizationGateway).retrieveByUserId(organization.userId());
        verify(organizationD2BConverter).process(organizations);
    }
}
