package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.TransactionUpsertB2DConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.usecase.UpsertTransactionUseCase;

public class UpsertTransactionInteractor implements UpsertTransactionUseCase {
    private final TransactionGateway transactionGateway;
    private final TransactionUpsertB2DConverter transactionUpsertB2DConverter;

    public UpsertTransactionInteractor(TransactionGateway transactionGateway,
                                       TransactionUpsertB2DConverter transactionUpsertB2DConverter) {
        this.transactionGateway = transactionGateway;
        this.transactionUpsertB2DConverter = transactionUpsertB2DConverter;
    }

    @Override
    public Completable execute(BoundaryTransactionUpsert input) {
        return Completable.fromAction(() -> transactionGateway.upsert(transactionUpsertB2DConverter.process(input).orElseThrow()));
    }
}
