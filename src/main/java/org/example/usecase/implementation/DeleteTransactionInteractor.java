package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.TransactionGateway;
import org.example.usecase.DeleteTransactionUseCase;

import java.util.UUID;

public class DeleteTransactionInteractor implements DeleteTransactionUseCase {
    private final TransactionGateway transactionGateway;

    public DeleteTransactionInteractor(TransactionGateway transactionGateway) {
        this.transactionGateway = transactionGateway;
    }

    @Override
    public Completable execute(UUID transactionId) {
        return Completable.fromAction(() -> {
            if (transactionGateway.retrieveById(transactionId).isEmpty())
                throw new ItemNotFoundException(String.format("Transaction with id [%s] not found", transactionId));
            transactionGateway.delete(transactionId);
        });
    }
}
