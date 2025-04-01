package org.example.factory;

import org.example.usecase.DeleteOrganizationUseCase;
import org.example.usecase.RetrieveOrganizationsUseCase;
import org.example.usecase.UpsertOrganizationUseCase;

public interface OrganizationUseCaseFactory {
    RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase();
    UpsertOrganizationUseCase createUpsertOrganizationUseCase();
    DeleteOrganizationUseCase createDeleteOrganizationUseCase();
}
