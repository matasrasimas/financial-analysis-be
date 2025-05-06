package org.example.factory.implementation;

import io.reactivex.rxjava3.core.Scheduler;
import org.example.factory.ExportUseCaseFactory;
import org.example.gateway.ExportGateway;
import org.example.gateway.TransactionGateway;
import org.example.usecase.ExportOrgUnitTransactionsUseCase;
import org.example.usecase.implementation.ExportOrgUnitTransactionsInteractor;

public class ExportUseCaseFactoryImpl implements ExportUseCaseFactory {
    private final Scheduler exportScheduler;
    private final ExportGateway exportGateway;
    private final TransactionGateway transactionGateway;

    public ExportUseCaseFactoryImpl(Scheduler exportScheduler,
                                    ExportGateway exportGateway,
                                    TransactionGateway transactionGateway) {
        this.exportScheduler = exportScheduler;
        this.exportGateway = exportGateway;
        this.transactionGateway = transactionGateway;
    }

    @Override
    public ExportOrgUnitTransactionsUseCase createExportOrgUnitTransactionsUseCase() {
        return new ExportOrgUnitTransactionsInteractor(exportScheduler, exportGateway, transactionGateway);
    }
}
