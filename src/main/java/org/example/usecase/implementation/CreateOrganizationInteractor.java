package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.converter.OrganizationCreateB2DConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.domain.OrgUnit;
import org.example.usecase.CreateOrganizationUseCase;

import java.util.UUID;

public class CreateOrganizationInteractor implements CreateOrganizationUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationCreateB2DConverter organizationCreateB2DConverter;
    private final OrgUnitD2BConverter orgUnitD2BConverter;

    public CreateOrganizationInteractor(OrganizationGateway organizationGateway,
                                        OrganizationCreateB2DConverter organizationCreateB2DConverter,
                                        OrgUnitD2BConverter orgUnitD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationCreateB2DConverter = organizationCreateB2DConverter;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
    }

    @Override
    public Single<BoundaryOrgUnit> execute(UUID requestorId, BoundaryOrganizationCreate input) {
        return Single.fromCallable(() -> {
            OrgUnit createdOrgUnit = organizationGateway.create(requestorId, organizationCreateB2DConverter.process(input).orElseThrow());
            return orgUnitD2BConverter.process(createdOrgUnit).orElseThrow();
        });
    }
}
