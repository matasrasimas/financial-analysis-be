package org.example.factory.implementation;

import org.example.converter.OrgUnitD2BConverter;
import org.example.converter.OrganizationB2DConverter;
import org.example.converter.OrganizationCreateB2DConverter;
import org.example.converter.OrganizationD2BConverter;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.gateway.OrganizationGateway;
import org.example.usecase.*;
import org.example.usecase.implementation.*;

public class OrganizationUseCaseFactoryImpl implements OrganizationUseCaseFactory {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;
    private final OrganizationB2DConverter organizationB2DConverter;
    private final OrganizationCreateB2DConverter organizationCreateB2DConverter;
    private final OrgUnitD2BConverter orgUnitD2BConverter;

    public OrganizationUseCaseFactoryImpl(OrganizationGateway organizationGateway,
                                          OrganizationD2BConverter organizationD2BConverter,
                                          OrganizationB2DConverter organizationB2DConverter,
                                          OrganizationCreateB2DConverter organizationCreateB2DConverter,
                                          OrgUnitD2BConverter orgUnitD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
        this.organizationB2DConverter = organizationB2DConverter;
        this.organizationCreateB2DConverter = organizationCreateB2DConverter;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
    }

    @Override
    public RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase() {
        return new RetrieveOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Override
    public RetrieveUserOrganizationUseCase createRetrieveUserOrganizationUseCase() {
        return new RetrieveUserOrganizationInteractor(organizationGateway, organizationD2BConverter);
    }

    @Override
    public CreateOrganizationUseCase createCreateOrganizationUseCase() {
        return new CreateOrganizationInteractor(organizationGateway, organizationCreateB2DConverter, orgUnitD2BConverter);
    }

    @Override
    public UpdateOrganizationUseCase createUpdateOrganizationUseCase() {
        return new UpdateOrganizationInteractor(organizationGateway, organizationB2DConverter);
    }

    @Override
    public DeleteOrganizationUseCase createDeleteOrganizationUseCase() {
        return new DeleteOrganizationInteractor(organizationGateway);
    }
}
