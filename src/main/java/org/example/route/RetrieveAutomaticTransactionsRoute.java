package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.AutomaticTransactionUseCaseFactory;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.rest.RestAutomaticTransaction;
import org.example.serialization.json.JsonSerializer;

import java.util.List;

public class RetrieveAutomaticTransactionsRoute extends AuthedRoute<List<BoundaryAutomaticTransaction>, List<RestAutomaticTransaction>> {
    private final AutomaticTransactionUseCaseFactory automaticTransactionUCFactory;
    private final AutomaticTransactionB2RConverter automaticTransactionB2RConverter;

    public RetrieveAutomaticTransactionsRoute(AuthenticationUseCaseFactory authUCFactory,
                                              AutomaticTransactionUseCaseFactory automaticTransactionUCFactory,
                                              JsonSerializer jsonSerializer,
                                              AutomaticTransactionB2RConverter automaticTransactionB2RConverter,
                                              JavalinExceptionHandler exceptionHandler) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.automaticTransactionB2RConverter = automaticTransactionB2RConverter;
        this.automaticTransactionUCFactory = automaticTransactionUCFactory;
    }

    @Override
    protected List<RestAutomaticTransaction> convert(List<BoundaryAutomaticTransaction> input) {
        return automaticTransactionB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryAutomaticTransaction>> processAuthedRequest(RequestWrapper request) {
        return automaticTransactionUCFactory.createRetrieveAutomaticTransactionsUseCase().execute();
    }
}
