package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrganizationB2DConverter;
import org.example.gateway.OrganizationGateway;
import org.example.model.boundary.BoundaryOrganization;
import org.example.usecase.UpdateOrganizationUseCase;

public class UpdateOrganizationInteractor implements UpdateOrganizationUseCase {
    private final OrganizationGateway organizationGateway;
    private final OrganizationB2DConverter organizationB2DConverter;

    public UpdateOrganizationInteractor(OrganizationGateway organizationGateway,
                                        OrganizationB2DConverter organizationB2DConverter) {
        this.organizationGateway = organizationGateway;
        this.organizationB2DConverter = organizationB2DConverter;
    }

    @Override
    public Completable execute(BoundaryOrganization input) {
        return Completable.fromAction(() -> organizationGateway.update(organizationB2DConverter.process(input).orElseThrow()));
    }
}
