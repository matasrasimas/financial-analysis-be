package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.model.boundary.BoundaryAutomaticTransaction;

public interface UpsertAutomaticTransactionUseCase {
    Completable execute(BoundaryAutomaticTransaction input);
}
