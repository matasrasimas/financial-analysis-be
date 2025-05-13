package org.example.usecase.implementation;

import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrganizationGateway;
import org.example.model.domain.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteOrganizationInteractorTest {

    @Mock
    private OrganizationGateway organizationGateway;

    private DeleteOrganizationInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteOrganizationInteractor(organizationGateway);
    }

    @Test
    void executeDeletesOrganizationIfExists() {
        UUID orgId = UUID.randomUUID();
        when(organizationGateway.retrieveById(orgId)).thenReturn(Optional.of(mock(Organization.class)));

        interactor.execute(orgId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(organizationGateway).retrieveById(orgId);
        verify(organizationGateway).delete(orgId);
    }

    @Test
    void executeThrowsIfOrganizationNotFound() {
        UUID orgId = UUID.randomUUID();
        when(organizationGateway.retrieveById(orgId)).thenReturn(Optional.empty());

        interactor.execute(orgId)
                .test()
                .assertError(e -> e instanceof ItemNotFoundException &&
                        e.getMessage().equals(String.format("organization by id [%s] not found", orgId)));

        verify(organizationGateway).retrieveById(orgId);
        verify(organizationGateway, never()).delete(any());
    }
}
