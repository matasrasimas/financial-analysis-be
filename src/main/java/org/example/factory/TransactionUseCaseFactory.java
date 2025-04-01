package org.example.factory;

import org.example.usecase.DeleteTransactionUseCase;
import org.example.usecase.RetrieveTransactionsUseCase;
import org.example.usecase.UpsertTransactionUseCase;

public interface TransactionUseCaseFactory {
    RetrieveTransactionsUseCase createRetrieveTransactionsUseCase();
    UpsertTransactionUseCase createUpsertTransactionUseCase();
    DeleteTransactionUseCase createDeleteTransactionUseCase();
}
