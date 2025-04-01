package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.OrganizationGateway;
import org.example.usecase.DeleteOrganizationUseCase;

import java.util.UUID;

public class DeleteOrganizationInteractor implements DeleteOrganizationUseCase {
    private final OrganizationGateway organizationGateway;

    public DeleteOrganizationInteractor(OrganizationGateway organizationGateway) {
        this.organizationGateway = organizationGateway;
    }

    @Override
    public Completable execute(UUID id) {
        return Completable.fromAction(() -> {
            if (organizationGateway.retrieveById(id).isEmpty())
                throw new ItemNotFoundException(String.format("organization by id [%s] not found", id));
            organizationGateway.delete(id);
        });
    }
}
