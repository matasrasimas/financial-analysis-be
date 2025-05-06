package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.StatisticsB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryStatistics;
import org.example.model.rest.RestStatistics;
import org.example.serialization.json.JsonSerializer;

import java.time.LocalDate;
import java.util.UUID;

import static org.example.common.RouteConstants.*;

public class RetrieveStatisticsRoute extends AuthedRoute<BoundaryStatistics, RestStatistics> {
    private final OrganizationUseCaseFactory organizationUseCaseFactory;
    private final StatisticsB2RConverter statisticsB2RConverter;

    public RetrieveStatisticsRoute(AuthenticationUseCaseFactory authUCFactory,
                                   JsonSerializer jsonSerializer,
                                   JavalinExceptionHandler exceptionHandler,
                                   OrganizationUseCaseFactory organizationUseCaseFactory,
                                   StatisticsB2RConverter statisticsB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.organizationUseCaseFactory = organizationUseCaseFactory;
        this.statisticsB2RConverter = statisticsB2RConverter;
    }

    @Override
    protected RestStatistics convert(BoundaryStatistics input) {
        return statisticsB2RConverter.process(input).orElseThrow();
    }

    @Override
    protected Single<BoundaryStatistics> processAuthedRequest(RequestWrapper request) {
        UUID orgId = UUID.fromString(request.getStringPathParam(ORGANIZATION_ID));
        return organizationUseCaseFactory.createRetrieveStatisticsUseCase().execute(orgId);
    }
}
