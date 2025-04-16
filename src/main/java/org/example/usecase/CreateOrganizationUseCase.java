package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.boundary.BoundaryOrganizationCreate;

import java.util.UUID;

public interface CreateOrganizationUseCase {
    Single<BoundaryOrgUnit> execute(UUID requestorId, BoundaryOrganizationCreate input);
}
