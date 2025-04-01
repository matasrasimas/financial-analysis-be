package org.example.factory.implementation;

import org.example.converter.OrgUnitB2DConverter;
import org.example.converter.OrgUnitD2BConverter;
import org.example.factory.OrgUnitUseCaseFactory;
import org.example.gateway.OrgUnitGateway;
import org.example.usecase.DeleteOrgUnitUseCase;
import org.example.usecase.RetrieveOrgUnitsUseCase;
import org.example.usecase.UpsertOrgUnitUseCase;
import org.example.usecase.implementation.DeleteOrgUnitInteractor;
import org.example.usecase.implementation.RetrieveOrgUnitsInteractor;
import org.example.usecase.implementation.UpsertOrgUnitInteractor;

public class OrgUnitUseCaseFactoryImpl implements OrgUnitUseCaseFactory {
    private final OrgUnitGateway orgUnitGateway;
    private final OrgUnitD2BConverter orgUnitD2BConverter;
    private final OrgUnitB2DConverter orgUnitB2DConverter;

    public OrgUnitUseCaseFactoryImpl(OrgUnitGateway orgUnitGateway,
                                     OrgUnitD2BConverter orgUnitD2BConverter,
                                     OrgUnitB2DConverter orgUnitB2DConverter) {
        this.orgUnitGateway = orgUnitGateway;
        this.orgUnitD2BConverter = orgUnitD2BConverter;
        this.orgUnitB2DConverter = orgUnitB2DConverter;
    }

    @Override
    public RetrieveOrgUnitsUseCase createRetrieveOrgUnitsUseCase() {
        return new RetrieveOrgUnitsInteractor(orgUnitGateway, orgUnitD2BConverter);
    }

    @Override
    public UpsertOrgUnitUseCase createUpsertOrgUnitUseCase() {
        return new UpsertOrgUnitInteractor(orgUnitGateway, orgUnitB2DConverter);
    }

    @Override
    public DeleteOrgUnitUseCase createDeleteOrgUnitUseCase() {
        return new DeleteOrgUnitInteractor(orgUnitGateway);
    }
}
