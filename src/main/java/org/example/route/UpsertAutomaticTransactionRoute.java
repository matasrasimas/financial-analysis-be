package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionB2RConverter;
import org.example.converter.AutomaticTransactionR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.AutomaticTransactionUseCaseFactory;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.rest.RestAutomaticTransaction;
import org.example.serialization.json.JsonSerializer;

public class UpsertAutomaticTransactionRoute extends AuthedRoute<BoundaryAutomaticTransaction, RestAutomaticTransaction> {
    private final AutomaticTransactionUseCaseFactory automaticTransactionUCFactory;
    private final AutomaticTransactionR2BConverter automaticTransactionR2BConverter;
    private final AutomaticTransactionB2RConverter automaticTransactionB2RConverter;

    public UpsertAutomaticTransactionRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                           JsonSerializer jsonSerializer,
                                           JavalinExceptionHandler exceptionHandler,
                                           AutomaticTransactionUseCaseFactory automaticTransactionUCFactory,
                                           AutomaticTransactionR2BConverter automaticTransactionR2BConverter,
                                           AutomaticTransactionB2RConverter automaticTransactionB2RConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, null, exceptionHandler);
        this.automaticTransactionUCFactory = automaticTransactionUCFactory;
        this.automaticTransactionR2BConverter = automaticTransactionR2BConverter;
        this.automaticTransactionB2RConverter = automaticTransactionB2RConverter;
    }

    @Override
    protected RestAutomaticTransaction convert(BoundaryAutomaticTransaction input) {
        return automaticTransactionB2RConverter.process(input).orElseThrow();
    }

    @Override
    protected Single<BoundaryAutomaticTransaction> processAuthedRequest(RequestWrapper request) {
        RestAutomaticTransaction reqBody = request.deserializeBody(RestAutomaticTransaction.class);
        BoundaryAutomaticTransaction boundaryAutomaticTransaction = automaticTransactionR2BConverter.process(reqBody).orElseThrow();

        return automaticTransactionUCFactory.createUpsertAutomaticTransactionUseCase().execute(boundaryAutomaticTransaction);
    }
}
