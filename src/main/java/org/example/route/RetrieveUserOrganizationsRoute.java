package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.rest.RestOrganization;
import org.example.serialization.json.JsonSerializer;

import java.util.List;
import java.util.UUID;

import static org.example.common.RouteConstants.USER_ID;

public class RetrieveUserOrganizationsRoute extends AuthedRoute<List<BoundaryOrganization>, List<RestOrganization>> {
    private final OrganizationUseCaseFactory organizationUseCaseFactory;
    private final OrganizationB2RConverter organizationB2RConverter;

    public RetrieveUserOrganizationsRoute(AuthenticationUseCaseFactory authUCFactory,
                                          JsonSerializer jsonSerializer,
                                          JavalinExceptionHandler exceptionHandler,
                                          OrganizationUseCaseFactory organizationUseCaseFactory,
                                          OrganizationB2RConverter organizationB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.organizationUseCaseFactory = organizationUseCaseFactory;
        this.organizationB2RConverter = organizationB2RConverter;
    }

    @Override
    protected List<RestOrganization> convert(List<BoundaryOrganization> input) {
        return organizationB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryOrganization>> processAuthedRequest(RequestWrapper request) {
        UUID userId = UUID.fromString(request.getStringPathParam(USER_ID));
        return organizationUseCaseFactory.createRetrieveUserOrganizationsUseCase().execute(userId);
    }
}
