package org.example.factory;

import org.example.usecase.*;

public interface OrgUnitUseCaseFactory {
    RetrieveOrgUnitsUseCase createRetrieveOrgUnitsUseCase();
    RetrieveOrganizationOrgUnitsUseCase createRetrieveOrganizationOrgUnitsUseCase();
    RetrieveOrgUnitByIdUseCase createRetrieveOrgUnitByIdUseCase();
    UpsertOrgUnitUseCase createUpsertOrgUnitUseCase();
    DeleteOrgUnitUseCase createDeleteOrgUnitUseCase();
}
