package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrgUnit;

import java.util.UUID;

public interface RetrieveOrgUnitByIdUseCase {
    Single<BoundaryOrgUnit> execute(UUID orgUnitId);
}
