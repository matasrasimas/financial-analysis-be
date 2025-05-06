package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitB2RConverter;
import org.example.converter.OrganizationB2RConverter;
import org.example.converter.OrganizationCreateR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.rest.RestOrgUnit;
import org.example.model.rest.RestOrganization;
import org.example.model.rest.RestOrganizationCreate;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

public class CreateOrganizationRoute extends AuthedRoute<BoundaryOrganization, RestOrganization> {
    private final OrganizationUseCaseFactory organizationUCFactory;
    private final OrganizationCreateR2BConverter organizationCreateR2BConverter;
    private final OrganizationB2RConverter organizationB2RConverter;

    public CreateOrganizationRoute(AuthenticationUseCaseFactory authUCFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   OrganizationUseCaseFactory organizationUCFactory,
                                   OrganizationCreateR2BConverter organizationCreateR2BConverter,
                                   OrganizationB2RConverter organizationB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.organizationUCFactory = organizationUCFactory;
        this.organizationCreateR2BConverter = organizationCreateR2BConverter;
        this.organizationB2RConverter = organizationB2RConverter;
    }

    @Override
    protected Single<BoundaryOrganization> processAuthedRequest(RequestWrapper request) {
        UUID requestorId = UUID.fromString(request.getRequestorId());
        RestOrganizationCreate reqBody = request.deserializeBody(RestOrganizationCreate.class);
        BoundaryOrganizationCreate boundaryOrganizationCreate = organizationCreateR2BConverter.process(reqBody).orElseThrow();
        return organizationUCFactory.createCreateOrganizationUseCase().execute(requestorId, boundaryOrganizationCreate);
    }

    @Override
    protected RestOrganization convert(BoundaryOrganization input) {
        return organizationB2RConverter.process(input).orElseThrow();
    }
}
