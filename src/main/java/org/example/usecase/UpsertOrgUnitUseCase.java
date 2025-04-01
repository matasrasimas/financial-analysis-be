package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.model.boundary.BoundaryOrgUnit;

public interface UpsertOrgUnitUseCase {
    Completable execute(BoundaryOrgUnit input);
}
