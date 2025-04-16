package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrgUnit;

import java.util.List;
import java.util.UUID;

public interface RetrieveOrganizationOrgUnitsUseCase {
    Single<List<BoundaryOrgUnit>> execute(UUID orgId);
}
