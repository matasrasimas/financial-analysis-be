package org.example.factory;

import org.example.usecase.*;

public interface OrganizationUseCaseFactory {
    RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase();
    RetrieveUserOrganizationUseCase createRetrieveUserOrganizationUseCase();
    CreateOrganizationUseCase createCreateOrganizationUseCase();
    UpdateOrganizationUseCase createUpdateOrganizationUseCase();
    DeleteOrganizationUseCase createDeleteOrganizationUseCase();
}
