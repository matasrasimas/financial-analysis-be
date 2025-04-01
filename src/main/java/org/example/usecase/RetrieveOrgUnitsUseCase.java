package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrgUnit;

import java.util.List;

public interface RetrieveOrgUnitsUseCase {
    Single<List<BoundaryOrgUnit>> execute();
}
