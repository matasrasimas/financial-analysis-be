package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;
import org.example.usecase.RetrieveOrgUnitByIdUseCase;

import java.util.Optional;
import java.util.UUID;

public class RetrieveOrgUnitByIdInteractor implements RetrieveOrgUnitByIdUseCase {
    private final OrgUnitGateway orgUnitGateway;
    private final OrgUnitD2BConverter orgUnitD2BConverter;

    public RetrieveOrgUnitByIdInteractor(OrgUnitGateway orgUnitGateway, OrgUnitD2BConverter orgUnitD2BConverter) {
        this.orgUnitGateway = orgUnitGateway;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
    }

    @Override
    public Single<BoundaryOrgUnit> execute(UUID orgUnitId) {
        return Single.fromCallable(() -> {
            Optional<OrgUnit> retrievedOrgUnit = orgUnitGateway.retrieveById(orgUnitId);
            if (retrievedOrgUnit.isEmpty())
                throw new ItemNotFoundException(String.format("orgUnit with id [%s] not found", orgUnitId));
            return orgUnitD2BConverter.process(retrievedOrgUnit.get()).orElseThrow();
        });
    }
}
