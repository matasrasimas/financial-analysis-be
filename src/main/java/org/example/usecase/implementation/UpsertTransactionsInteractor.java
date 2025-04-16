package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.converter.TransactionUpsertB2DConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.domain.Transaction;
import org.example.usecase.UpsertTransactionsUseCase;

import java.util.List;

public class UpsertTransactionsInteractor implements UpsertTransactionsUseCase {
    private final TransactionGateway transactionGateway;
    private final TransactionUpsertB2DConverter transactionUpsertB2DConverter;
    private final TransactionD2BConverter transactionD2BConverter;

    public UpsertTransactionsInteractor(TransactionGateway transactionGateway,
                                        TransactionUpsertB2DConverter transactionUpsertB2DConverter,
                                        TransactionD2BConverter transactionD2BConverter) {
        this.transactionGateway = transactionGateway;
        this.transactionUpsertB2DConverter = transactionUpsertB2DConverter;
        this.transactionD2BConverter = transactionD2BConverter;
    }

    @Override
    public Single<BoundaryTransaction> execute(List<BoundaryTransactionUpsert> input) {
        return Single.fromCallable(() -> {
            List<Transaction> upsertedTransactions = transactionGateway.upsert(transactionUpsertB2DConverter.process(input));
            return transactionD2BConverter.process(upsertedTransactions.getFirst()).orElseThrow();
        });
    }
}
