package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import org.example.gateway.ExportGateway;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryFile;
import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionsReportRow;
import org.example.usecase.ExportOrgUnitTransactionsUseCase;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExportOrgUnitTransactionsInteractor implements ExportOrgUnitTransactionsUseCase {
    private final Scheduler exportScheduler;
    private final ExportGateway exportGateway;
    private final TransactionGateway transactionGateway;

    public ExportOrgUnitTransactionsInteractor(Scheduler exportScheduler,
                                               ExportGateway exportGateway,
                                               TransactionGateway transactionGateway) {
        this.exportScheduler = exportScheduler;
        this.exportGateway = exportGateway;
        this.transactionGateway = transactionGateway;
    }

    @Override
    public Single<BoundaryFile> execute(String orgUnitId, LocalDate from, LocalDate to) {
        return Single.fromCallable(() -> {
                    List<Transaction> transactions = transactionGateway.retrieveByOrgUnitId(UUID.fromString(orgUnitId), from, to);
                    List<TransactionsReportRow> reportRows = transactions.stream()
                            .map(transaction -> new TransactionsReportRow(
                                    String.format("%s-%s", transaction.createdAt().getYear(), mapMonthToText(transaction.createdAt().getMonthValue())),
                                    transaction.createdAt().toString(),
                                    transaction.title(),
                                    transaction.amount()))
                            .sorted(Comparator.comparing(TransactionsReportRow::getDate).thenComparing(TransactionsReportRow::getTitle))
                            .collect(Collectors.toUnmodifiableList());

                    double totalAmount = transactions.stream().map(Transaction::amount).reduce(0.0, Double::sum);
                    String formattedPeriod = String.format("%s - %s", from, to);
                    ByteArrayOutputStream output = exportGateway.exportTransactions(formattedPeriod, reportRows, totalAmount);
                    String fileName = String.format("Transakciju ataskaita %s", formattedPeriod);
                    return new BoundaryFile(output, fileName);
                })
                .subscribeOn(exportScheduler)
                .observeOn(exportScheduler)
                .timeout(7, TimeUnit.MINUTES, exportScheduler);
    }

    private String mapMonthToText(int month) {
        return switch (month) {
            case 1 -> "01";
            case 2 -> "02";
            case 3 -> "03";
            case 4 -> "04";
            case 5 -> "05";
            case 6 -> "06";
            case 7 -> "07";
            case 8 -> "08";
            case 9 -> "09";
            case 10 -> "10";
            case 11 -> "11";
            case 12 -> "12";
            default -> "";
        };
    }
}
