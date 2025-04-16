package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RetrieveOrgUnitTransactionsUseCase {
    Single<List<BoundaryTransaction>> execute(UUID orgUnitId, LocalDate from, LocalDate to);
}
