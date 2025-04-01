package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.model.boundary.BoundaryTransactionUpsert;

public interface UpsertTransactionUseCase {
    Completable execute(BoundaryTransactionUpsert input);
}
