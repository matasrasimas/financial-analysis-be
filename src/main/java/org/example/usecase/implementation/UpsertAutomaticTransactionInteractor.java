package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionB2DConverter;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;
import org.example.usecase.UpsertAutomaticTransactionUseCase;

public class UpsertAutomaticTransactionInteractor implements UpsertAutomaticTransactionUseCase {
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final AutomaticTransactionB2DConverter automaticTransactionB2DConverter;
    private final AutomaticTransactionD2BConverter automaticTransactionD2BConverter;

    public UpsertAutomaticTransactionInteractor(AutomaticTransactionGateway automaticTransactionGateway,
                                                AutomaticTransactionB2DConverter automaticTransactionB2DConverter,
                                                AutomaticTransactionD2BConverter automaticTransactionD2BConverter) {
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.automaticTransactionB2DConverter = automaticTransactionB2DConverter;
        this.automaticTransactionD2BConverter = automaticTransactionD2BConverter;
    }

    @Override
    public Single<BoundaryAutomaticTransaction> execute(BoundaryAutomaticTransaction input) {
        return Single.fromCallable(() -> {
            AutomaticTransaction upsertedAutomaticTransaction = automaticTransactionGateway.upsert(automaticTransactionB2DConverter.process(input).orElseThrow());
            return automaticTransactionD2BConverter.process(upsertedAutomaticTransaction).orElseThrow();
        });
    }
}
