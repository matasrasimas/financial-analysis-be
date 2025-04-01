package org.example.factory;

import org.example.usecase.DeleteAutomaticTransactionUseCase;
import org.example.usecase.RetrieveAutomaticTransactionsUseCase;
import org.example.usecase.UpsertAutomaticTransactionUseCase;

public interface AutomaticTransactionUseCaseFactory {
    RetrieveAutomaticTransactionsUseCase createRetrieveAutomaticTransactionsUseCase();
    UpsertAutomaticTransactionUseCase createUpsertAutomaticTransactionUseCase();
    DeleteAutomaticTransactionUseCase createDeleteAutomaticTransactionUseCase();
}
