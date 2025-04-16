package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryAutomaticTransaction;

import java.util.List;
import java.util.UUID;

public interface RetrieveOrgUnitAutomaticTransactionsUseCase {
    Single<List<BoundaryAutomaticTransaction>> execute(UUID orgUnitId);
}
