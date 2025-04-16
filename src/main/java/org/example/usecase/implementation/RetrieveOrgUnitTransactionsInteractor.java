package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.usecase.RetrieveOrgUnitTransactionsUseCase;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RetrieveOrgUnitTransactionsInteractor implements RetrieveOrgUnitTransactionsUseCase {
    private final TransactionGateway transactionGateway;
    private final TransactionD2BConverter transactionD2BConverter;

    public RetrieveOrgUnitTransactionsInteractor(TransactionGateway transactionGateway,
                                                 TransactionD2BConverter transactionD2BConverter) {
        this.transactionGateway = transactionGateway;
        this.transactionD2BConverter = transactionD2BConverter;
    }

    @Override
    public Single<List<BoundaryTransaction>> execute(UUID orgUnitId, LocalDate from, LocalDate to) {
        return Single.fromCallable(() ->
                transactionD2BConverter.process(transactionGateway.retrieveByOrgUnitId(orgUnitId, from, to))
                        .stream()
                        .sorted(Comparator.comparing(BoundaryTransaction::createdAt, Comparator.reverseOrder()))
                        .collect(Collectors.toList()));
    }
}
