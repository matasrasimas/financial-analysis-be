package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.AutomaticTransactionB2DConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.usecase.UpsertAutomaticTransactionUseCase;

public class UpsertAutomaticTransactionInteractor implements UpsertAutomaticTransactionUseCase {
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final AutomaticTransactionB2DConverter automaticTransactionB2DConverter;

    public UpsertAutomaticTransactionInteractor(AutomaticTransactionGateway automaticTransactionGateway,
                                                AutomaticTransactionB2DConverter automaticTransactionB2DConverter) {
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.automaticTransactionB2DConverter = automaticTransactionB2DConverter;
    }

    @Override
    public Completable execute(BoundaryAutomaticTransaction input) {
        return Completable.fromAction(() -> automaticTransactionGateway.upsert(automaticTransactionB2DConverter.process(input).orElseThrow()));
    }
}
