package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.usecase.RetrieveOrgUnitAutomaticTransactionsUseCase;

import java.util.List;
import java.util.UUID;

public class RetrieveOrgUnitAutomaticTransactionsInteractor implements RetrieveOrgUnitAutomaticTransactionsUseCase {
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final AutomaticTransactionD2BConverter automaticTransactionD2BConverter;

    public RetrieveOrgUnitAutomaticTransactionsInteractor(AutomaticTransactionGateway automaticTransactionGateway,
                                                          AutomaticTransactionD2BConverter automaticTransactionD2BConverter) {
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.automaticTransactionD2BConverter = automaticTransactionD2BConverter;
    }

    @Override
    public Single<List<BoundaryAutomaticTransaction>> execute(UUID orgUnitId) {
        return Single.fromCallable(() -> automaticTransactionD2BConverter.process(automaticTransactionGateway.retrieveByOrgUnitId(orgUnitId)));
    }
}
