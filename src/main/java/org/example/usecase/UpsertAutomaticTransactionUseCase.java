package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryAutomaticTransaction;

public interface UpsertAutomaticTransactionUseCase {
    Single<BoundaryAutomaticTransaction> execute(BoundaryAutomaticTransaction input);
}
