package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.ExportUseCaseFactory;
import org.example.model.boundary.BoundaryFile;
import org.example.serialization.json.JsonSerializer;

import java.time.LocalDate;

import static org.example.common.RouteConstants.*;

public class ExportOrgUnitTransactionsRoute extends XlsxRoute {
    private final ExportUseCaseFactory exportUseCaseFactory;

    public ExportOrgUnitTransactionsRoute(AuthenticationUseCaseFactory useCaseFactory,
                                          ExportUseCaseFactory exportUseCaseFactory,
                                          JsonSerializer jsonSerializer,
                                          JavalinExceptionHandler exceptionHandler) {
        super(useCaseFactory, jsonSerializer, exceptionHandler);
        this.exportUseCaseFactory = exportUseCaseFactory;
    }

    @Override
    protected Single<BoundaryFile> processAuthedRequest(RequestWrapper request) {
        String orgUnitId = request.getStringPathParam(ORG_UNIT_ID);
        LocalDate from = request.parseDateParam(FROM);
        LocalDate to = request.parseDateParam(TO);
        BoundaryFile boundaryFile = exportUseCaseFactory.createExportOrgUnitTransactionsUseCase()
                .execute(orgUnitId, from, to)
                .onErrorResumeNext(RxUtil.mapCheckedTimeoutException())
                .blockingGet();
        return Single.just(boundaryFile);

    }
}
