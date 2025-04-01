package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryTransaction;

import java.util.List;

public interface RetrieveTransactionsUseCase {
    Single<List<BoundaryTransaction>> execute();
}
