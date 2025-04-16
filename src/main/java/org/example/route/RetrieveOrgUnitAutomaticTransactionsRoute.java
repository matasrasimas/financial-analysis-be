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
import java.util.UUID;

import static org.example.common.RouteConstants.ORG_UNIT_ID;

public class RetrieveOrgUnitAutomaticTransactionsRoute extends AuthedRoute<List<BoundaryAutomaticTransaction>, List<RestAutomaticTransaction>> {
    private final AutomaticTransactionUseCaseFactory automaticTransactionUseCaseFactory;
    private final AutomaticTransactionB2RConverter automaticTransactionB2RConverter;

    public RetrieveOrgUnitAutomaticTransactionsRoute(AuthenticationUseCaseFactory authUCFactory,
                                                     JsonSerializer jsonSerializer,
                                                     JavalinExceptionHandler exceptionHandler,
                                                     AutomaticTransactionUseCaseFactory automaticTransactionUseCaseFactory,
                                                     AutomaticTransactionB2RConverter automaticTransactionB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.automaticTransactionUseCaseFactory = automaticTransactionUseCaseFactory;
        this.automaticTransactionB2RConverter = automaticTransactionB2RConverter;
    }

    @Override
    protected List<RestAutomaticTransaction> convert(List<BoundaryAutomaticTransaction> input) {
        return automaticTransactionB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryAutomaticTransaction>> processAuthedRequest(RequestWrapper request) {
        UUID orgUnitId = UUID.fromString(request.getStringPathParam(ORG_UNIT_ID));
        return automaticTransactionUseCaseFactory.createRetrieveOrgUnitAutomaticTransactionsUseCase().execute(orgUnitId);
    }
}
