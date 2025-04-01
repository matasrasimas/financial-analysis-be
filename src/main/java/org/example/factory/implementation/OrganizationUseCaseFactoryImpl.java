package org.example.factory.implementation;

import org.example.converter.OrganizationB2DConverter;
import org.example.converter.OrganizationD2BConverter;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.gateway.OrganizationGateway;
import org.example.usecase.DeleteOrganizationUseCase;
import org.example.usecase.RetrieveOrganizationsUseCase;
import org.example.usecase.UpsertOrganizationUseCase;
import org.example.usecase.implementation.DeleteOrganizationInteractor;
import org.example.usecase.implementation.RetrieveOrganizationsInteractor;
import org.example.usecase.implementation.UpsertOrganizationInteractor;

public class OrganizationUseCaseFactoryImpl implements OrganizationUseCaseFactory {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;
    private final OrganizationB2DConverter organizationB2DConverter;

    public OrganizationUseCaseFactoryImpl(OrganizationGateway organizationGateway,
                                          OrganizationD2BConverter organizationD2BConverter,
                                          OrganizationB2DConverter organizationB2DConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
        this.organizationB2DConverter = organizationB2DConverter;
    }

    @Override
    public RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase() {
        return new RetrieveOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Override
    public UpsertOrganizationUseCase createUpsertOrganizationUseCase() {
        return new UpsertOrganizationInteractor(organizationGateway, organizationB2DConverter);
    }

    @Override
    public DeleteOrganizationUseCase createDeleteOrganizationUseCase() {
        return new DeleteOrganizationInteractor(organizationGateway);
    }
}
