package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.rest.RestTransaction;
import org.example.serialization.json.JsonSerializer;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.example.common.RouteConstants.*;

public class RetrieveOrganizationTransactionsRoute extends AuthedRoute<List<BoundaryTransaction>, List<RestTransaction>> {
    private final TransactionUseCaseFactory transactionUseCaseFactory;
    private final TransactionB2RConverter transactionB2RConverter;

    public RetrieveOrganizationTransactionsRoute(AuthenticationUseCaseFactory authUCFactory,
                                                 JsonSerializer jsonSerializer,
                                                 JavalinExceptionHandler exceptionHandler,
                                                 TransactionUseCaseFactory transactionUseCaseFactory,
                                                 TransactionB2RConverter transactionB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.transactionUseCaseFactory = transactionUseCaseFactory;
        this.transactionB2RConverter = transactionB2RConverter;
    }

    @Override
    protected List<RestTransaction> convert(List<BoundaryTransaction> input) {
        return transactionB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryTransaction>> processAuthedRequest(RequestWrapper request) {
        UUID orgId = UUID.fromString(request.getStringPathParam(ORGANIZATION_ID));
        LocalDate from = request.parseDateParam(FROM);
        LocalDate to = request.parseDateParam(TO);
        return transactionUseCaseFactory.createRetrieveOrganizationTransactionsUseCase().execute(orgId, from, to);
    }
}
