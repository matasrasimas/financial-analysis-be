package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryAutomaticTransaction;

import java.util.List;

public interface RetrieveAutomaticTransactionsUseCase {
    Single<List<BoundaryAutomaticTransaction>> execute();
}
