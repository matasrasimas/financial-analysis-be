package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.ORGANIZATION_ID;

public class DeleteOrganizationRoute extends WithoutResponseBodyRoute {
    private final OrganizationUseCaseFactory organizationUCFactory;

    public DeleteOrganizationRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   OrganizationUseCaseFactory organizationUCFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.organizationUCFactory = organizationUCFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID orgId = UUID.fromString(request.getStringPathParam(ORGANIZATION_ID));

        return organizationUCFactory.createDeleteOrganizationUseCase().execute(orgId);
    }
}
