package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrgUnitUseCaseFactory;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.rest.RestOrgUnit;
import org.example.serialization.json.JsonSerializer;

import java.util.List;
import java.util.UUID;

import static org.example.common.RouteConstants.ORGANIZATION_ID;

public class RetrieveOrganizationOrgUnitsRoute extends AuthedRoute<List<BoundaryOrgUnit>, List<RestOrgUnit>> {
    private final OrgUnitUseCaseFactory orgUnitUseCaseFactory;
    private final OrgUnitB2RConverter orgUnitB2RConverter;

    public RetrieveOrganizationOrgUnitsRoute(AuthenticationUseCaseFactory authUCFactory,
                                             JsonSerializer jsonSerializer,
                                             JavalinExceptionHandler exceptionHandler,
                                             OrgUnitUseCaseFactory orgUnitUseCaseFactory,
                                             OrgUnitB2RConverter orgUnitB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.orgUnitUseCaseFactory = orgUnitUseCaseFactory;
        this.orgUnitB2RConverter = orgUnitB2RConverter;
    }

    @Override
    protected List<RestOrgUnit> convert(List<BoundaryOrgUnit> input) {
        return orgUnitB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryOrgUnit>> processAuthedRequest(RequestWrapper request) {
        UUID orgId = UUID.fromString(request.getStringPathParam(ORGANIZATION_ID));
        return orgUnitUseCaseFactory.createRetrieveOrganizationOrgUnitsUseCase().execute(orgId);
    }
}
