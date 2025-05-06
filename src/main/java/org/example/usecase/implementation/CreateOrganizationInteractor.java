package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.converter.OrganizationCreateB2DConverter;
import org.example.converter.OrganizationD2BConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.domain.OrgUnit;
import org.example.model.domain.Organization;
import org.example.usecase.CreateOrganizationUseCase;

import java.util.UUID;

public class CreateOrganizationInteractor implements CreateOrganizationUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationCreateB2DConverter organizationCreateB2DConverter;
    private final OrganizationD2BConverter organizationD2BConverter;

    public CreateOrganizationInteractor(OrganizationGateway organizationGateway,
                                        OrganizationCreateB2DConverter organizationCreateB2DConverter,
                                        OrganizationD2BConverter organizationD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationCreateB2DConverter = organizationCreateB2DConverter;
        this.organizationD2BConverter = organizationD2BConverter;
    }

    @Override
    public Single<BoundaryOrganization> execute(UUID requestorId, BoundaryOrganizationCreate input) {
        return Single.fromCallable(() -> {
            Organization createdOrganization = organizationGateway.create(requestorId, organizationCreateB2DConverter.process(input).orElseThrow());
            return organizationD2BConverter.process(createdOrganization).orElseThrow();
        });
    }
}
