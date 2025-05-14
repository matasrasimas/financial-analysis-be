package org.example.factory.implementation;

import io.reactivex.rxjava3.core.Scheduler;
import org.example.factory.AutomaticProcessUseCaseFactory;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.gateway.TransactionGateway;
import org.example.usecase.ProcessAutomaticTransactionsUseCase;
import org.example.temp.ProcessAutomaticTransactionsInteractor;

public class AutomaticProcessUseCaseFactoryImpl implements AutomaticProcessUseCaseFactory {
    private final Scheduler scheduler;
    private final TransactionGateway transactionGateway;
    private final AutomaticTransactionGateway automaticTransactionGateway;

    public AutomaticProcessUseCaseFactoryImpl(Scheduler scheduler,
                                              TransactionGateway transactionGateway,
                                              AutomaticTransactionGateway automaticTransactionGateway) {
        this.scheduler = scheduler;
        this.transactionGateway = transactionGateway;
        this.automaticTransactionGateway = automaticTransactionGateway;
    }

    @Override
    public ProcessAutomaticTransactionsUseCase createProcessAutomaticTransactionsUseCase() {
        return new ProcessAutomaticTransactionsInteractor(scheduler, automaticTransactionGateway, transactionGateway);
    }
}
