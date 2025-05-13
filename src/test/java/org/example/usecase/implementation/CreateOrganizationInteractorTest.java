package org.example.usecase.implementation;

import org.example.converter.OrganizationCreateB2DConverter;
import org.example.converter.OrganizationD2BConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.domain.Organization;
import org.example.model.domain.OrganizationCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrganizationInteractorTest {

    @Mock
    private OrganizationGateway organizationGateway;
    @Mock
    private OrganizationCreateB2DConverter organizationCreateB2DConverter;
    @Mock
    private OrganizationD2BConverter organizationD2BConverter;

    private CreateOrganizationInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new CreateOrganizationInteractor(
                organizationGateway,
                organizationCreateB2DConverter,
                organizationD2BConverter
        );
    }

    @Test
    void executeWithValidInputReturnsBoundaryOrganization() {
        UUID requestorId = UUID.randomUUID();
        BoundaryOrganizationCreate input = mock(BoundaryOrganizationCreate.class);
        OrganizationCreate domainOrgCreate = mock(OrganizationCreate.class);
        Organization domainOrg = mock(Organization.class);
        BoundaryOrganization boundaryOrg = mock(BoundaryOrganization.class);

        when(organizationCreateB2DConverter.process(input)).thenReturn(Optional.of(domainOrgCreate));
        when(organizationGateway.create(requestorId, domainOrgCreate)).thenReturn(domainOrg);
        when(organizationD2BConverter.process(domainOrg)).thenReturn(Optional.of(boundaryOrg));

        interactor.execute(requestorId, input)
                .test()
                .assertComplete()
                .assertValue(boundaryOrg);

        verify(organizationCreateB2DConverter).process(input);
        verify(organizationGateway).create(requestorId, domainOrgCreate);
        verify(organizationD2BConverter).process(domainOrg);
    }

    @Test
    void executeWithMissingDomainConversionThrowsException() {
        UUID requestorId = UUID.randomUUID();
        BoundaryOrganizationCreate input = mock(BoundaryOrganizationCreate.class);

        when(organizationCreateB2DConverter.process(input)).thenReturn(Optional.empty());

        interactor.execute(requestorId, input)
                .test()
                .assertError(NoSuchElementException.class);

        verify(organizationCreateB2DConverter).process(input);
        verifyNoInteractions(organizationGateway);
        verifyNoInteractions(organizationD2BConverter);
    }

    @Test
    void executeWithMissingBoundaryConversionThrowsException() {
        UUID requestorId = UUID.randomUUID();
        BoundaryOrganizationCreate input = mock(BoundaryOrganizationCreate.class);
        OrganizationCreate domainOrgCreate = mock(OrganizationCreate.class);
        Organization domainOrg = mock(Organization.class);

        when(organizationCreateB2DConverter.process(input)).thenReturn(Optional.of(domainOrgCreate));
        when(organizationGateway.create(requestorId, domainOrgCreate)).thenReturn(domainOrg);
        when(organizationD2BConverter.process(domainOrg)).thenReturn(Optional.empty());

        interactor.execute(requestorId, input)
                .test()
                .assertError(NoSuchElementException.class);

        verify(organizationCreateB2DConverter).process(input);
        verify(organizationGateway).create(requestorId, domainOrgCreate);
        verify(organizationD2BConverter).process(domainOrg);
    }
}