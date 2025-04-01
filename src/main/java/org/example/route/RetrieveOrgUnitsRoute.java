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

public class RetrieveOrgUnitsRoute extends AuthedRoute<List<BoundaryOrgUnit>, List<RestOrgUnit>> {
    private final OrgUnitUseCaseFactory orgunitUCFactory;
    private final OrgUnitB2RConverter orgUnitB2RConverter;

    public RetrieveOrgUnitsRoute(AuthenticationUseCaseFactory authUCFactory,
                                 OrgUnitUseCaseFactory orgunitUCFactory,
                                 JsonSerializer jsonSerializer,
                                 OrgUnitB2RConverter orgUnitB2RConverter,
                                 JavalinExceptionHandler exceptionHandler) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.orgunitUCFactory = orgunitUCFactory;
        this.orgUnitB2RConverter = orgUnitB2RConverter;
    }

    @Override
    protected List<RestOrgUnit> convert(List<BoundaryOrgUnit> input) {
        return orgUnitB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryOrgUnit>> processAuthedRequest(RequestWrapper request) {
        return orgunitUCFactory.createRetrieveOrgUnitsUseCase().execute();
    }
}
