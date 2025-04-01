package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.gateway.OrgUnitGateway;
import org.example.usecase.DeleteOrgUnitUseCase;

import java.util.UUID;

public class DeleteOrgUnitInteractor implements DeleteOrgUnitUseCase {
    private final OrgUnitGateway orgUnitGateway;

    public DeleteOrgUnitInteractor(OrgUnitGateway orgUnitGateway) {
        this.orgUnitGateway = orgUnitGateway;
    }

    @Override
    public Completable execute(UUID id) {
        return Completable.fromAction(() -> orgUnitGateway.delete(id));
    }
}
