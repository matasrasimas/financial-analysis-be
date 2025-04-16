package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;
import org.example.usecase.RetrieveUserOrganizationUseCase;

import java.util.Optional;
import java.util.UUID;

public class RetrieveUserOrganizationInteractor implements RetrieveUserOrganizationUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;

    public RetrieveUserOrganizationInteractor(OrganizationGateway organizationGateway,
                                              OrganizationD2BConverter organizationD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
    }

    @Override
    public Single<BoundaryOrganization> execute(UUID userId) {
        return Single.fromCallable(() -> {
            Optional<Organization> organization = organizationGateway.retrieveByUserId(userId);
            if(organization.isEmpty())
                throw new ItemNotFoundException(String.format("organization with user id [%s] not found", userId));
            return organizationD2BConverter.process(organization.get()).orElseThrow();
        });
    }
}
