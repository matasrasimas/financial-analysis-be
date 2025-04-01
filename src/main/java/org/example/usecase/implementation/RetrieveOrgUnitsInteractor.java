package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.usecase.RetrieveOrgUnitsUseCase;

import java.util.List;

public class RetrieveOrgUnitsInteractor implements RetrieveOrgUnitsUseCase {
    private final OrgUnitGateway orgUnitGateway;
    private final OrgUnitD2BConverter orgUnitD2BConverter;

    public RetrieveOrgUnitsInteractor(OrgUnitGateway orgUnitGateway,
                                      OrgUnitD2BConverter orgUnitD2BConverter) {
        this.orgUnitGateway = orgUnitGateway;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
    }

    @Override
    public Single<List<BoundaryOrgUnit>> execute() {
        return Single.fromCallable(() -> orgUnitD2BConverter.process(orgUnitGateway.retrieve()));
    }
}
