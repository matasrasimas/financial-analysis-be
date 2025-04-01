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

public class RetrieveOrganizationsRoute extends AuthedRoute<List<BoundaryOrganization>, List<RestOrganization>> {
    private final OrganizationUseCaseFactory organizationUCFactory;
    private final OrganizationB2RConverter organizationB2RConverter;

    public RetrieveOrganizationsRoute(AuthenticationUseCaseFactory authUCFactory,
                                     OrganizationUseCaseFactory organizationUCFactory,
                                     JsonSerializer jsonSerializer,
                                      OrganizationB2RConverter organizationB2RConverter,
                                     JavalinExceptionHandler exceptionHandler) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.organizationB2RConverter = organizationB2RConverter;
        this.organizationUCFactory = organizationUCFactory;
    }

    @Override
    protected List<RestOrganization> convert(List<BoundaryOrganization> input) {
        return organizationB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryOrganization>> processAuthedRequest(RequestWrapper request) {
        return organizationUCFactory.createRetrieveOrganizationsUseCase().execute();
    }
}
