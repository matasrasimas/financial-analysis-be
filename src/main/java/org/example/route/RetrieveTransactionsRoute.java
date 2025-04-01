package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.rest.RestTransaction;
import org.example.serialization.json.JsonSerializer;

import java.util.List;

public class RetrieveTransactionsRoute extends AuthedRoute<List<BoundaryTransaction>, List<RestTransaction>> {
    private final TransactionUseCaseFactory transactionUCFactory;
    private final TransactionB2RConverter transactionB2RConverter;

    public RetrieveTransactionsRoute(AuthenticationUseCaseFactory authUCFactory,
                                     TransactionUseCaseFactory transactionUCFactory,
                                     JsonSerializer jsonSerializer,
                                     TransactionB2RConverter transactionB2RConverter,
                                     JavalinExceptionHandler exceptionHandler) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.transactionB2RConverter = transactionB2RConverter;
        this.transactionUCFactory = transactionUCFactory;
    }

    @Override
    protected List<RestTransaction> convert(List<BoundaryTransaction> input) {
        return transactionB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryTransaction>> processAuthedRequest(RequestWrapper request) {
        return transactionUCFactory.createRetrieveTransactionsUseCase().execute();
    }
}
