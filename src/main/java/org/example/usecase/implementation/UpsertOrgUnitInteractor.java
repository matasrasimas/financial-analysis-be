package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrgUnitB2DConverter;
import org.example.gateway.OrgUnitGateway;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.usecase.UpsertOrgUnitUseCase;

public class UpsertOrgUnitInteractor implements UpsertOrgUnitUseCase {
    private final OrgUnitGateway orgUnitGateway;
    private final OrgUnitB2DConverter orgUnitB2DConverter;

    public UpsertOrgUnitInteractor(OrgUnitGateway orgUnitGateway,
                                   OrgUnitB2DConverter orgUnitB2DConverter) {
        this.orgUnitGateway = orgUnitGateway;
        this.orgUnitB2DConverter = orgUnitB2DConverter;
    }

    @Override
    public Completable execute(BoundaryOrgUnit input) {
        return Completable.fromAction(() -> orgUnitGateway.upsert(orgUnitB2DConverter.process(input).orElseThrow()));
    }
}
