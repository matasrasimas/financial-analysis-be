package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationD2BConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.usecase.RetrieveOrganizationsUseCase;

import java.util.List;

public class RetrieveOrganizationsInteractor implements RetrieveOrganizationsUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;

    public RetrieveOrganizationsInteractor(OrganizationGateway organizationGateway,
                                           OrganizationD2BConverter organizationD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
    }

    @Override
    public Single<List<BoundaryOrganization>> execute() {
        return Single.fromCallable(() -> organizationD2BConverter.process(organizationGateway.retrieve()));
    }
}
