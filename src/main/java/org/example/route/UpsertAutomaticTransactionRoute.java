package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.AutomaticTransactionR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.AutomaticTransactionUseCaseFactory;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.rest.RestAutomaticTransaction;
import org.example.serialization.json.JsonSerializer;

public class UpsertAutomaticTransactionRoute extends WithoutResponseBodyRoute {
    private final AutomaticTransactionUseCaseFactory automaticTransactionUCFactory;
    private final AutomaticTransactionR2BConverter automaticTransactionR2BConverter;

    public UpsertAutomaticTransactionRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                           JsonSerializer jsonSerializer,
                                           JavalinExceptionHandler exceptionHandler,
                                           AutomaticTransactionUseCaseFactory automaticTransactionUCFactory,
                                           AutomaticTransactionR2BConverter automaticTransactionR2BConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.automaticTransactionUCFactory = automaticTransactionUCFactory;
        this.automaticTransactionR2BConverter = automaticTransactionR2BConverter;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        RestAutomaticTransaction reqBody = request.deserializeBody(RestAutomaticTransaction.class);
        BoundaryAutomaticTransaction boundaryAutomaticTransaction = automaticTransactionR2BConverter.process(reqBody).orElseThrow();

        return automaticTransactionUCFactory.createUpsertAutomaticTransactionUseCase().execute(boundaryAutomaticTransaction);
    }
}
