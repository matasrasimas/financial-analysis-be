package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionB2RConverter;
import org.example.converter.TransactionUpsertR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.rest.RestTransaction;
import org.example.model.rest.RestTransactionUpsert;
import org.example.serialization.json.JsonSerializer;

import java.util.Arrays;
import java.util.List;

public class UpsertTransactionsRoute extends AuthedRoute<BoundaryTransaction, RestTransaction> {
    private final TransactionUseCaseFactory transactionUCFactory;
    private final TransactionUpsertR2BConverter transactionUpsertR2BConverter;
    private final TransactionB2RConverter transactionB2RConverter;

    public UpsertTransactionsRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   TransactionUseCaseFactory transactionUCFactory,
                                   TransactionUpsertR2BConverter transactionUpsertR2BConverter,
                                   TransactionB2RConverter transactionB2RConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, null, exceptionHandler);
        this.transactionUCFactory = transactionUCFactory;
        this.transactionUpsertR2BConverter = transactionUpsertR2BConverter;
        this.transactionB2RConverter = transactionB2RConverter;
    }

    @Override
    protected RestTransaction convert(BoundaryTransaction input) {
        return transactionB2RConverter.process(input).orElseThrow();
    }

    @Override
    protected Single<BoundaryTransaction> processAuthedRequest(RequestWrapper request) {
        List<RestTransactionUpsert> reqBody = Arrays.stream(request.deserializeBody(RestTransactionUpsert[].class)).toList();
        List<BoundaryTransactionUpsert> boundaryTransactions = transactionUpsertR2BConverter.process(reqBody);

        return transactionUCFactory.createUpsertTransactionsUseCase().execute(boundaryTransactions);
    }
}
