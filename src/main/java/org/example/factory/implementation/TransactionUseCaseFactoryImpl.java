package org.example.factory.implementation;

import org.example.converter.TransactionD2BConverter;
import org.example.converter.TransactionUpsertB2DConverter;
import org.example.factory.TransactionUseCaseFactory;
import org.example.gateway.TransactionGateway;
import org.example.usecase.DeleteTransactionUseCase;
import org.example.usecase.RetrieveTransactionsUseCase;
import org.example.usecase.UpsertTransactionUseCase;
import org.example.usecase.implementation.DeleteTransactionInteractor;
import org.example.usecase.implementation.RetrieveTransactionsInteractor;
import org.example.usecase.implementation.UpsertTransactionInteractor;

public class TransactionUseCaseFactoryImpl implements TransactionUseCaseFactory {
    private final TransactionGateway transactionGateway;
    private final TransactionD2BConverter transactionD2BConverter;
    private final TransactionUpsertB2DConverter transactionUpsertB2DConverter;

    public TransactionUseCaseFactoryImpl(TransactionGateway transactionGateway,
                                         TransactionD2BConverter transactionD2BConverter,
                                         TransactionUpsertB2DConverter transactionUpsertB2DConverter) {
        this.transactionGateway = transactionGateway;
        this.transactionD2BConverter = transactionD2BConverter;
        this.transactionUpsertB2DConverter = transactionUpsertB2DConverter;
    }

    @Override
    public RetrieveTransactionsUseCase createRetrieveTransactionsUseCase() {
        return new RetrieveTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }

    @Override
    public UpsertTransactionUseCase createUpsertTransactionUseCase() {
        return new UpsertTransactionInteractor(transactionGateway, transactionUpsertB2DConverter);
    }

    @Override
    public DeleteTransactionUseCase createDeleteTransactionUseCase() {
        return new DeleteTransactionInteractor(transactionGateway);
    }
}
