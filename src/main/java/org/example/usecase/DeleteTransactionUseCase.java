package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;

import java.util.UUID;

public interface DeleteTransactionUseCase {
    Completable execute(UUID transactionId);
}
