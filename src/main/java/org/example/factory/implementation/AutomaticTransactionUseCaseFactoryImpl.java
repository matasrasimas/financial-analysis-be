package org.example.factory.implementation;

import org.example.converter.AutomaticTransactionB2DConverter;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.factory.AutomaticTransactionUseCaseFactory;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.usecase.DeleteAutomaticTransactionUseCase;
import org.example.usecase.RetrieveAutomaticTransactionsUseCase;
import org.example.usecase.UpsertAutomaticTransactionUseCase;
import org.example.usecase.implementation.DeleteAutomaticTransactionInteractor;
import org.example.usecase.implementation.RetrieveAutomaticTransactionsInteractor;
import org.example.usecase.implementation.UpsertAutomaticTransactionInteractor;

public class AutomaticTransactionUseCaseFactoryImpl implements AutomaticTransactionUseCaseFactory {
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final AutomaticTransactionD2BConverter automaticTransactionD2BConverter;
    private final AutomaticTransactionB2DConverter automaticTransactionB2DConverter;

    public AutomaticTransactionUseCaseFactoryImpl(AutomaticTransactionGateway automaticTransactionGateway,
                                                  AutomaticTransactionD2BConverter automaticTransactionD2BConverter,
                                                  AutomaticTransactionB2DConverter automaticTransactionB2DConverter) {
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.automaticTransactionD2BConverter = automaticTransactionD2BConverter;
        this.automaticTransactionB2DConverter = automaticTransactionB2DConverter;
    }

    @Override
    public RetrieveAutomaticTransactionsUseCase createRetrieveAutomaticTransactionsUseCase() {
        return new RetrieveAutomaticTransactionsInteractor(automaticTransactionGateway, automaticTransactionD2BConverter);
    }

    @Override
    public UpsertAutomaticTransactionUseCase createUpsertAutomaticTransactionUseCase() {
        return new UpsertAutomaticTransactionInteractor(automaticTransactionGateway, automaticTransactionB2DConverter);
    }

    @Override
    public DeleteAutomaticTransactionUseCase createDeleteAutomaticTransactionUseCase() {
        return new DeleteAutomaticTransactionInteractor(automaticTransactionGateway);
    }
}
