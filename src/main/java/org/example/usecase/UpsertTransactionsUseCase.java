package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.boundary.BoundaryTransactionUpsert;

import java.util.List;

public interface UpsertTransactionsUseCase {
    Single<BoundaryTransaction> execute(List<BoundaryTransactionUpsert> input);
}
