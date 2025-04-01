package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrgUnitUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.ORG_UNIT_ID;

public class DeleteOrgUnitRoute extends WithoutResponseBodyRoute {
    private final OrgUnitUseCaseFactory orgUnitUCFactory;

    public DeleteOrgUnitRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                              JsonSerializer jsonSerializer,
                              JavalinExceptionHandler exceptionHandler,
                              OrgUnitUseCaseFactory orgUnitUCFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.orgUnitUCFactory = orgUnitUCFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID orgUnitId = UUID.fromString(request.getStringPathParam(ORG_UNIT_ID));

        return orgUnitUCFactory.createDeleteOrgUnitUseCase().execute(orgUnitId);
    }
}
