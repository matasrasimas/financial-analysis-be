package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrgUnitB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrgUnitUseCaseFactory;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.rest.RestOrgUnit;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.ORG_UNIT_ID;

public class RetrieveOrgUnitByIdRoute extends AuthedRoute<BoundaryOrgUnit, RestOrgUnit> {
    private final OrgUnitUseCaseFactory orgUnitUCFactory;
    private final OrgUnitB2RConverter orgUnitB2RConverter;

    public RetrieveOrgUnitByIdRoute(AuthenticationUseCaseFactory authUCFactory,
                                    JsonSerializer jsonSerializer,
                                    JavalinExceptionHandler exceptionHandler,
                                    OrgUnitUseCaseFactory orgUnitUCFactory,
                                    OrgUnitB2RConverter orgUnitB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.orgUnitUCFactory = orgUnitUCFactory;
        this.orgUnitB2RConverter = orgUnitB2RConverter;
    }

    @Override
    protected RestOrgUnit convert(BoundaryOrgUnit input) {
        return orgUnitB2RConverter.process(input).orElseThrow();
    }

    @Override
    protected Single<BoundaryOrgUnit> processAuthedRequest(RequestWrapper request) {
        UUID orgUnitId = UUID.fromString(request.getStringPathParam(ORG_UNIT_ID));
        return orgUnitUCFactory.createRetrieveOrgUnitByIdUseCase().execute(orgUnitId);
    }
}
