package org.example.factory;

import org.example.usecase.*;

public interface OrganizationUseCaseFactory {
    RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase();
    RetrieveUserOrganizationsUseCase createRetrieveUserOrganizationsUseCase();
    CreateOrganizationUseCase createCreateOrganizationUseCase();
    UpdateOrganizationUseCase createUpdateOrganizationUseCase();
    DeleteOrganizationUseCase createDeleteOrganizationUseCase();
    RetrieveStatisticsUseCase createRetrieveStatisticsUseCase();
}
