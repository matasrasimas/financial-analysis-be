package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitB2RConverter;
import org.example.converter.OrganizationCreateR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.rest.RestOrgUnit;
import org.example.model.rest.RestOrganizationCreate;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

public class CreateOrganizationRoute extends AuthedRoute<BoundaryOrgUnit, RestOrgUnit> {
    private final OrganizationUseCaseFactory organizationUCFactory;
    private final OrganizationCreateR2BConverter organizationCreateR2BConverter;
    private final OrgUnitB2RConverter orgUnitB2RConverter;

    public CreateOrganizationRoute(AuthenticationUseCaseFactory authUCFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   OrganizationUseCaseFactory organizationUCFactory,
                                   OrganizationCreateR2BConverter organizationCreateR2BConverter,
                                   OrgUnitB2RConverter orgUnitB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.organizationUCFactory = organizationUCFactory;
        this.organizationCreateR2BConverter = organizationCreateR2BConverter;
        this.orgUnitB2RConverter = orgUnitB2RConverter;
    }

    @Override
    protected Single<BoundaryOrgUnit> processAuthedRequest(RequestWrapper request) {
        UUID requestorId = UUID.fromString(request.getRequestorId());
        RestOrganizationCreate reqBody = request.deserializeBody(RestOrganizationCreate.class);
        BoundaryOrganizationCreate boundaryOrganizationCreate = organizationCreateR2BConverter.process(reqBody).orElseThrow();
        return organizationUCFactory.createCreateOrganizationUseCase().execute(requestorId, boundaryOrganizationCreate);
    }

    @Override
    protected RestOrgUnit convert(BoundaryOrgUnit input) {
        return orgUnitB2RConverter.process(input).orElseThrow();
    }
}
