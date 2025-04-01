package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.TRANSACTION_ID;

public class DeleteTransactionRoute extends WithoutResponseBodyRoute {
    private final TransactionUseCaseFactory transactionUCFactory;

    public DeleteTransactionRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                  JsonSerializer jsonSerializer,
                                  JavalinExceptionHandler exceptionHandler,
                                  TransactionUseCaseFactory transactionUCFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.transactionUCFactory = transactionUCFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID transactionId = UUID.fromString(request.getStringPathParam(TRANSACTION_ID));

        return transactionUCFactory.createDeleteTransactionUseCase().execute(transactionId);
    }
}
