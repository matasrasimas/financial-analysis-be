package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.usecase.RetrieveTransactionsUseCase;

import java.util.List;

public class RetrieveTransactionsInteractor implements RetrieveTransactionsUseCase {
    private final TransactionGateway transactionGateway;
    private final TransactionD2BConverter transactionD2BConverter;

    public RetrieveTransactionsInteractor(TransactionGateway transactionGateway,
                                          TransactionD2BConverter transactionD2BConverter) {
        this.transactionGateway = transactionGateway;
        this.transactionD2BConverter = transactionD2BConverter;
    }

    @Override
    public Single<List<BoundaryTransaction>> execute() {
        return Single.fromCallable(() -> transactionD2BConverter.process(transactionGateway.retrieve()));
    }
}
