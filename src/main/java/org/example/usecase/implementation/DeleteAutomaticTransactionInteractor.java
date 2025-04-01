package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.usecase.DeleteAutomaticTransactionUseCase;

import java.util.UUID;

public class DeleteAutomaticTransactionInteractor implements DeleteAutomaticTransactionUseCase {
    private final AutomaticTransactionGateway automaticTransactionGateway;

    public DeleteAutomaticTransactionInteractor(AutomaticTransactionGateway automaticTransactionGateway) {
        this.automaticTransactionGateway = automaticTransactionGateway;
    }

    @Override
    public Completable execute(UUID id) {
        return Completable.fromAction(() ->  {
            if (automaticTransactionGateway.retrieveById(id).isEmpty())
                throw new ItemNotFoundException(String.format("automatic-transaction with id [%s] not found", id));
            automaticTransactionGateway.delete(id);
        });
    }
}
