package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.usecase.RetrieveAutomaticTransactionsUseCase;

import java.util.List;

public class RetrieveAutomaticTransactionsInteractor implements RetrieveAutomaticTransactionsUseCase {
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final AutomaticTransactionD2BConverter automaticTransactionD2BConverter;

    public RetrieveAutomaticTransactionsInteractor(AutomaticTransactionGateway automaticTransactionGateway,
                                                   AutomaticTransactionD2BConverter automaticTransactionD2BConverter) {
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.automaticTransactionD2BConverter = automaticTransactionD2BConverter;
    }

    @Override
    public Single<List<BoundaryAutomaticTransaction>> execute() {
        return Single.fromCallable(() -> automaticTransactionD2BConverter.process(automaticTransactionGateway.retrieve()));
    }
}
