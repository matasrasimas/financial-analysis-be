package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;
import org.example.usecase.RetrieveUserOrganizationsUseCase;

import java.util.List;
import java.util.UUID;

public class RetrieveUserOrganizationsInteractor implements RetrieveUserOrganizationsUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;

    public RetrieveUserOrganizationsInteractor(OrganizationGateway organizationGateway,
                                               OrganizationD2BConverter organizationD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
    }

    @Override
    public Single<List<BoundaryOrganization>> execute(UUID userId) {
        return Single.fromCallable(() -> {
            List<Organization> retrievedOrganizations = organizationGateway.retrieveByUserId(userId);
            if(retrievedOrganizations.isEmpty())
                throw new ItemNotFoundException(String.format("organizations with user id [%s] not found", userId));
            return organizationD2BConverter.process(retrievedOrganizations);
        });
    }
}
