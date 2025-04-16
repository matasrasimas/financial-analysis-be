package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrganizationR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.rest.RestOrganization;
import org.example.serialization.json.JsonSerializer;

public class UpdateOrganizationRoute extends WithoutResponseBodyRoute {
    private final OrganizationUseCaseFactory organizationUCFactory;
    private final OrganizationR2BConverter organizationR2BConverter;

    public UpdateOrganizationRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   OrganizationUseCaseFactory organizationUCFactory,
                                   OrganizationR2BConverter organizationR2BConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.organizationUCFactory = organizationUCFactory;
        this.organizationR2BConverter = organizationR2BConverter;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        RestOrganization reqBody = request.deserializeBody(RestOrganization.class);
        BoundaryOrganization boundaryOrganization = organizationR2BConverter.process(reqBody).orElseThrow();

        return organizationUCFactory.createUpdateOrganizationUseCase().execute(boundaryOrganization);
    }
}
