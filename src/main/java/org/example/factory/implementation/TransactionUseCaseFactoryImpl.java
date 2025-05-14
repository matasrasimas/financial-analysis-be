package org.example.factory.implementation;

import org.example.converter.TransactionD2BConverter;
import org.example.converter.TransactionUpsertB2DConverter;
import org.example.factory.TransactionUseCaseFactory;
import org.example.gateway.TransactionGateway;
import org.example.temp.GenerateTransactionsFromFileInteractor;
import org.example.usecase.*;
import org.example.usecase.implementation.*;

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
    public RetrieveOrgUnitTransactionsUseCase createRetrieveOrgUnitTransactionsUseCase() {
        return new RetrieveOrgUnitTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }

    @Override
    public UpsertTransactionsUseCase createUpsertTransactionsUseCase() {
        return new UpsertTransactionsInteractor(transactionGateway, transactionUpsertB2DConverter, transactionD2BConverter);
    }

    @Override
    public DeleteTransactionUseCase createDeleteTransactionUseCase() {
        return new DeleteTransactionInteractor(transactionGateway);
    }

    @Override
    public GenerateTransactionsFromFileUseCase createGenerateTransactionsFromFileUseCase() {
        return new GenerateTransactionsFromFileInteractor();
    }

    @Override
    public RetrieveOrganizationTransactionsUseCase createRetrieveOrganizationTransactionsUseCase() {
        return new RetrieveOrganizationTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }
}
