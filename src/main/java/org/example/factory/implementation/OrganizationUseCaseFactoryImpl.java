package org.example.factory.implementation;

import org.example.converter.*;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.gateway.OrganizationGateway;
import org.example.gateway.TransactionGateway;
import org.example.temp.RetrieveStatisticsInteractor;
import org.example.usecase.*;
import org.example.usecase.implementation.*;

public class OrganizationUseCaseFactoryImpl implements OrganizationUseCaseFactory {
    private final OrganizationGateway organizationGateway;
    private final OrganizationD2BConverter organizationD2BConverter;
    private final OrganizationB2DConverter organizationB2DConverter;
    private final OrganizationCreateB2DConverter organizationCreateB2DConverter;
    private final OrgUnitD2BConverter orgUnitD2BConverter;
    private final TransactionGateway transactionGateway;
    private final TransactionD2BConverter transactionD2BConverter;

    public OrganizationUseCaseFactoryImpl(OrganizationGateway organizationGateway,
                                          OrganizationD2BConverter organizationD2BConverter,
                                          OrganizationB2DConverter organizationB2DConverter,
                                          OrganizationCreateB2DConverter organizationCreateB2DConverter,
                                          OrgUnitD2BConverter orgUnitD2BConverter,
                                          TransactionGateway transactionGateway,
                                          TransactionD2BConverter transactionD2BConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationD2BConverter = organizationD2BConverter;
        this.organizationB2DConverter = organizationB2DConverter;
        this.organizationCreateB2DConverter = organizationCreateB2DConverter;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
        this.transactionGateway = transactionGateway;
        this.transactionD2BConverter = transactionD2BConverter;
    }

    @Override
    public RetrieveOrganizationsUseCase createRetrieveOrganizationsUseCase() {
        return new RetrieveOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Override
    public RetrieveUserOrganizationsUseCase createRetrieveUserOrganizationsUseCase() {
        return new RetrieveUserOrganizationsInteractor(organizationGateway, organizationD2BConverter);
    }

    @Override
    public CreateOrganizationUseCase createCreateOrganizationUseCase() {
        return new CreateOrganizationInteractor(organizationGateway, organizationCreateB2DConverter, organizationD2BConverter);
    }

    @Override
    public UpdateOrganizationUseCase createUpdateOrganizationUseCase() {
        return new UpdateOrganizationInteractor(organizationGateway, organizationB2DConverter);
    }

    @Override
    public DeleteOrganizationUseCase createDeleteOrganizationUseCase() {
        return new DeleteOrganizationInteractor(organizationGateway);
    }

    @Override
    public RetrieveStatisticsUseCase createRetrieveStatisticsUseCase() {
        return new RetrieveStatisticsInteractor(transactionGateway, transactionD2BConverter, organizationGateway);
    }
}
