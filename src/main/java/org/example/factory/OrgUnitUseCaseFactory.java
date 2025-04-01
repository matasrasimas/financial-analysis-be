package org.example.factory;

import org.example.usecase.DeleteOrgUnitUseCase;
import org.example.usecase.RetrieveOrgUnitsUseCase;
import org.example.usecase.UpsertOrgUnitUseCase;

public interface OrgUnitUseCaseFactory {
    RetrieveOrgUnitsUseCase createRetrieveOrgUnitsUseCase();
    UpsertOrgUnitUseCase createUpsertOrgUnitUseCase();
    DeleteOrgUnitUseCase createDeleteOrgUnitUseCase();
}
