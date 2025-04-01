package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.OrgUnitR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrgUnitUseCaseFactory;
import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.rest.RestOrgUnit;
import org.example.serialization.json.JsonSerializer;

public class UpsertOrgUnitRoute extends WithoutResponseBodyRoute {
    private final OrgUnitUseCaseFactory orgUnitUCFactory;
    private final OrgUnitR2BConverter orgUnitR2BConverter;

    public UpsertOrgUnitRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                              JsonSerializer jsonSerializer,
                              JavalinExceptionHandler exceptionHandler,
                              OrgUnitUseCaseFactory orgUnitUCFactory,
                              OrgUnitR2BConverter orgUnitR2BConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.orgUnitUCFactory = orgUnitUCFactory;
        this.orgUnitR2BConverter = orgUnitR2BConverter;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        RestOrgUnit reqBody = request.deserializeBody(RestOrgUnit.class);
        BoundaryOrgUnit boundaryOrgUnit = orgUnitR2BConverter.process(reqBody).orElseThrow();

        return orgUnitUCFactory.createUpsertOrgUnitUseCase().execute(boundaryOrgUnit);
    }
}
