package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.usecase.RetrieveOrganizationOrgUnitsUseCase;

import java.util.List;
import java.util.UUID;

public class RetrieveOrganizationOrgUnitsInteractor implements RetrieveOrganizationOrgUnitsUseCase {
    private final OrgUnitGateway orgUnitGateway;
    private final OrgUnitD2BConverter orgUnitD2BConverter;

    public RetrieveOrganizationOrgUnitsInteractor(OrgUnitGateway orgUnitGateway,
                                                  OrgUnitD2BConverter orgUnitD2BConverter) {
        this.orgUnitGateway = orgUnitGateway;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
    }

    @Override
    public Single<List<BoundaryOrgUnit>> execute(UUID orgId) {
        return Single.fromCallable(() -> orgUnitD2BConverter.process(orgUnitGateway.retrieveByOrgId(orgId)));
    }
}
