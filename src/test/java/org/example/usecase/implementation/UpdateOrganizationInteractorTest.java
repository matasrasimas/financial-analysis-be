package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrganizationB2DConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;
import org.example.usecase.UpdateOrganizationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UpdateOrganizationInteractorTest {

    @Mock
    private OrganizationGateway organizationGateway;

    @Mock
    private OrganizationB2DConverter organizationB2DConverter;

    private UpdateOrganizationUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new UpdateOrganizationInteractor(organizationGateway, organizationB2DConverter);
    }

    @Test
    void executeShouldUpdateOrganizationSuccessfully() {
        // Prepare test data
        UUID organizationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BoundaryOrganization boundaryOrganization = new BoundaryOrganization(
                organizationId, userId, "Organization A", Optional.of("ABC123"), Optional.of("Address 1"), 100000);

        Organization domainOrganization = new Organization(
                organizationId, userId, "Organization A", Optional.of("ABC123"), Optional.of("Address 1"), 100000);

        // Mock behavior
        when(organizationB2DConverter.process(boundaryOrganization)).thenReturn(Optional.of(domainOrganization));

        // Execute the use case
        Completable result = interactor.execute(boundaryOrganization);

        // Assertions
        result.test().assertComplete();

        // Verify interactions with mocks
        verify(organizationB2DConverter).process(boundaryOrganization);
        verify(organizationGateway).update(domainOrganization);
    }

    @Test
    void executeShouldThrowExceptionIfConversionFails() {
        // Prepare test data
        UUID organizationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BoundaryOrganization boundaryOrganization = new BoundaryOrganization(
                organizationId, userId, "Organization A", Optional.of("ABC123"), Optional.of("Address 1"), 100000);

        // Mock behavior
        when(organizationB2DConverter.process(boundaryOrganization)).thenReturn(Optional.empty());

        // Execute the use case and verify that an exception is thrown
        interactor.execute(boundaryOrganization)
                .test()
                .assertError(e -> e.getClass().equals(NoSuchElementException.class));

        // Verify interactions with mocks
        verify(organizationB2DConverter).process(boundaryOrganization);
        verify(organizationGateway, never()).update(any()); // Ensure update was not called
    }

    @Test
    void executeShouldCallUpdateOnGatewayWhenConversionIsSuccessful() {
        // Prepare test data
        UUID organizationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BoundaryOrganization boundaryOrganization = new BoundaryOrganization(
                organizationId, userId, "Organization B", Optional.of("XYZ789"), Optional.of("Address 2"), 200000);

        Organization domainOrganization = new Organization(
                organizationId, userId, "Organization B", Optional.of("XYZ789"), Optional.of("Address 2"), 200000);

        // Mock behavior
        when(organizationB2DConverter.process(boundaryOrganization)).thenReturn(Optional.of(domainOrganization));

        // Execute the use case
        Completable result = interactor.execute(boundaryOrganization);

        // Assertions
        result.test().assertComplete();

        // Verify that the correct method was called
        verify(organizationB2DConverter).process(boundaryOrganization);
        verify(organizationGateway).update(domainOrganization);
    }
}
