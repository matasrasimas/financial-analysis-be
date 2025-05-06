package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.gateway.TransactionGateway;
import org.example.model.domain.AutomaticTransaction;
import org.example.model.domain.TransactionUpsert;
import org.example.usecase.ProcessAutomaticTransactionsUseCase;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProcessAutomaticTransactionsInteractor implements ProcessAutomaticTransactionsUseCase {
    private static final Logger LOGGER = LogManager.getLogger(ProcessAutomaticTransactionsInteractor.class);
    private final Scheduler scheduler;
    private final AutomaticTransactionGateway automaticTransactionGateway;
    private final TransactionGateway transactionGateway;

    public ProcessAutomaticTransactionsInteractor(Scheduler scheduler,
                                                  AutomaticTransactionGateway automaticTransactionGateway,
                                                  TransactionGateway transactionGateway) {
        this.scheduler = scheduler;
        this.automaticTransactionGateway = automaticTransactionGateway;
        this.transactionGateway = transactionGateway;
    }

    @Override
    public Completable execute() {
        return Observable.interval(65, TimeUnit.SECONDS)
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .concatMapCompletable(ignored -> Completable.fromAction(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    Map<AutomaticTransaction, LocalDateTime> latestDateByAutomaticTransaction = automaticTransactionGateway.getLatestDateByAutomaticTransaction();
                    latestDateByAutomaticTransaction.keySet().forEach(automaticTransaction -> {
                        int durationMinutes = switch (automaticTransaction.durationUnit()) {
                            case MINUTES -> automaticTransaction.duration();
                            case HOURS -> automaticTransaction.duration() * 60;
                            case DAYS -> automaticTransaction.duration() * 1440;
                        };
                        LocalDateTime latestTransactionDate = latestDateByAutomaticTransaction.get(automaticTransaction);
                        if (Duration.between(latestTransactionDate, now).toMinutes() > durationMinutes) {
                            transactionGateway.upsert(convertToTransaction(automaticTransaction));
                            automaticTransactionGateway.updateLatestTransactionDate(automaticTransaction.id());
                        }
                    });
                }))
                .doOnError(err -> LOGGER.error("Error while processing automatic transactions", err))
                .retryWhen(throwableFlowable -> Flowable.interval(30, TimeUnit.SECONDS).onBackpressureDrop());
    }

    private TransactionUpsert convertToTransaction(AutomaticTransaction input) {
        return new TransactionUpsert(
                UUID.randomUUID(),
                input.orgUnitId(),
                null,
                input.amount(),
                input.title(),
                LocalDate.now(),
                false
        );
    }
}
