package org.example.factory;

import org.example.usecase.*;

public interface TransactionUseCaseFactory {
    RetrieveTransactionsUseCase createRetrieveTransactionsUseCase();
    RetrieveOrgUnitTransactionsUseCase createRetrieveOrgUnitTransactionsUseCase();
    UpsertTransactionsUseCase createUpsertTransactionsUseCase();
    DeleteTransactionUseCase createDeleteTransactionUseCase();
    GenerateTransactionsFromFileUseCase createGenerateTransactionsFromFileUseCase();
    RetrieveOrganizationTransactionsUseCase createRetrieveOrganizationTransactionsUseCase();
}
