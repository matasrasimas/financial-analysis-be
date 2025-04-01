package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.TransactionUpsertR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.rest.RestTransactionUpsert;
import org.example.serialization.json.JsonSerializer;

public class UpsertTransactionRoute extends WithoutResponseBodyRoute {
    private final TransactionUseCaseFactory transactionUCFactory;
    private final TransactionUpsertR2BConverter transactionUpsertR2BConverter;

    public UpsertTransactionRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                  JsonSerializer jsonSerializer,
                                  JavalinExceptionHandler exceptionHandler,
                                  TransactionUseCaseFactory transactionUCFactory,
                                  TransactionUpsertR2BConverter transactionUpsertR2BConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.transactionUCFactory = transactionUCFactory;
        this.transactionUpsertR2BConverter = transactionUpsertR2BConverter;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        RestTransactionUpsert reqBody = request.deserializeBody(RestTransactionUpsert.class);
        BoundaryTransactionUpsert boundaryTransaction = transactionUpsertR2BConverter.process(reqBody).orElseThrow();

        return transactionUCFactory.createUpsertTransactionUseCase().execute(boundaryTransaction);
    }
}
