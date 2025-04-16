package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;

public interface ProcessAutomaticTransactionsUseCase {
    Completable execute();
}
