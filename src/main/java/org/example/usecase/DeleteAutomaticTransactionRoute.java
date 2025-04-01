package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.AutomaticTransactionUseCaseFactory;
import org.example.route.RequestWrapper;
import org.example.route.WithoutResponseBodyRoute;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.AUTOMATIC_TRANSACTION_ID;

public class DeleteAutomaticTransactionRoute extends WithoutResponseBodyRoute {
    private final AutomaticTransactionUseCaseFactory automaticTransactionUCFactory;

    public DeleteAutomaticTransactionRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                           JsonSerializer jsonSerializer,
                                           JavalinExceptionHandler exceptionHandler,
                                           AutomaticTransactionUseCaseFactory automaticTransactionUCFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.automaticTransactionUCFactory = automaticTransactionUCFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID transactionId = UUID.fromString(request.getStringPathParam(AUTOMATIC_TRANSACTION_ID));

        return automaticTransactionUCFactory.createDeleteAutomaticTransactionUseCase().execute(transactionId);
    }
}
