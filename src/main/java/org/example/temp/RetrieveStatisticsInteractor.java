package org.example.temp;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.OrganizationGateway;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryStatistics;
import org.example.model.domain.Organization;
import org.example.model.domain.Transaction;
import org.example.usecase.RetrieveStatisticsUseCase;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class RetrieveStatisticsInteractor implements RetrieveStatisticsUseCase {
    private final TransactionGateway transactionGateway;
    private final TransactionD2BConverter transactionD2BConverter;
    private final OrganizationGateway organizationGateway;

    public RetrieveStatisticsInteractor(TransactionGateway transactionGateway,
                                        TransactionD2BConverter transactionD2BConverter,
                                        OrganizationGateway organizationGateway) {
        this.transactionGateway = transactionGateway;
        this.transactionD2BConverter = transactionD2BConverter;
        this.organizationGateway = organizationGateway;
    }

    @Override
    public Single<BoundaryStatistics> execute(UUID orgId) {
        return Single.fromCallable(() -> {
            Organization organization = organizationGateway.retrieveById(orgId).orElseThrow();

            LocalDate lastDayOfPreviousYear = LocalDate.now()
                    .withDayOfYear(1)
                    .minusDays(1);

            LocalDate firstDayOfPreviousYear = LocalDate.now()
                    .minusYears(1)
                    .withDayOfYear(1);
            LocalDate firstDayOfCurrentYear = LocalDate.now()
                    .withDayOfYear(1);

            LocalDate lastDayOfCurrentYear = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31);

            List<Transaction> transactions = transactionGateway.retrieveByOrgId(orgId);

            List<Transaction> previousYearTransactions = transactions.stream()
                    .filter(transaction -> transaction.createdAt().isBefore(lastDayOfPreviousYear))
                    .collect(Collectors.toList());
            double previousYearTotalAmount = previousYearTransactions.stream().map(Transaction::amount).reduce(0.0, Double::sum);

            List<Transaction> currentYearTransactions = transactions.stream()
                    .filter(transaction -> !transaction.createdAt().isBefore(firstDayOfCurrentYear) && !transaction.createdAt().isAfter(lastDayOfCurrentYear))
                    .collect(Collectors.toList());
            double currentYearTotalAmount = currentYearTransactions.stream().map(Transaction::amount).reduce(0.0, Double::sum);

            List<LocalDate> previousYearMonths = new ArrayList<>();
            LocalDate current = firstDayOfPreviousYear.withDayOfMonth(1);

            while (!current.isAfter(lastDayOfPreviousYear)) {
                previousYearMonths.add(current);
                current = current.plusMonths(1);
            }

            List<LocalDate> currentYearMonths = new ArrayList<>();
            current = firstDayOfCurrentYear;
            while (!current.isAfter(lastDayOfCurrentYear)) {
                currentYearMonths.add(current);
                current = current.plusMonths(1);
            }

            List<String> xValues = List.of("Sausis", "Vasaris", "Kovas", "Balandis", "Gegužė", "Birželis", "Liepa", "Rugpjūtis", "Rugsėjis", "Spalis", "Lapkritis", "Gruodis");

            List<Double> previousYearYValues = previousYearMonths.stream()
                    .map(month -> transactions.stream()
                            .filter(transaction -> transaction.createdAt().getMonth() == month.getMonth() && transaction.createdAt().getYear() == month.getYear())
                            .map(Transaction::amount)
                            .reduce(0.0, Double::sum))
                    .collect(Collectors.toUnmodifiableList());

            List<Double> currentYearYValues = currentYearMonths.stream()
                    .map(month -> transactions.stream()
                            .filter(transaction -> transaction.createdAt().getMonth() == month.getMonth() && transaction.createdAt().getYear() == month.getYear())
                            .map(Transaction::amount)
                            .reduce(0.0, Double::sum))
                    .collect(Collectors.toUnmodifiableList());

            double goalCompletionPercentage = currentYearTotalAmount / organization.yearlyGoal() * 100;

            Map<String, List<Transaction>> groupedByTitle = currentYearTransactions.stream()
                    .collect(Collectors.groupingBy(t -> t.title().toLowerCase()));

            Map<Transaction, Long> transactionsCount = groupedByTitle.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getValue().get(0), // pick the first Transaction as key
                            e -> (long) e.getValue().size() // count of transactions with that title
                    ));

            Map<Transaction, Long> top5MostFrequentTransactions = transactionsCount.entrySet().stream()
                    .sorted(Map.Entry.<Transaction, Long>comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            Map<String, Double> monthlyProfit = currentYearTransactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> mapMonthToText(t.createdAt().getMonthValue()),
                            Collectors.summingDouble(Transaction::amount)
                    ));

            Map.Entry<String, Double> mostProfitableMonth = monthlyProfit.entrySet().stream()
                    .max(Map.Entry.comparingByValue())  // Find the entry with the maximum value
                    .orElseThrow(() -> new NoSuchElementException("No transactions found"));

            Map.Entry<String, Double> mostUnprofitableMonth = monthlyProfit.entrySet().stream()
                    .min(Map.Entry.comparingByValue())  // Find the entry with the minimum value
                    .orElseThrow(() -> new NoSuchElementException("No transactions found"));

            // Step 2: Sort the months chronologically
            List<Map.Entry<String, Double>> sortedMonths = monthlyProfit.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList());

            // Step 3: Calculate percent changes
            List<Double> percentChanges = new ArrayList<>();
            for (int i = 1; i < sortedMonths.size(); i++) {
                double previousAmount = sortedMonths.get(i - 1).getValue();
                double currentAmount = sortedMonths.get(i).getValue();
                if (previousAmount != 0) { // Avoid division by zero
                    double percentChange = ((currentAmount - previousAmount) / previousAmount) * 100;
                    percentChanges.add(percentChange);
                }
            }

            // Step 4: Calculate average trend
            double averageTrend = percentChanges.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            return new BoundaryStatistics(
                    firstDayOfPreviousYear,
                    lastDayOfCurrentYear,
                    xValues,
                    previousYearYValues,
                    currentYearYValues,
                    currentYearTransactions.size(),
                    previousYearTransactions.size(),
                    goalCompletionPercentage,
                    currentYearTotalAmount,
                    previousYearTotalAmount,
                    top5MostFrequentTransactions.entrySet().stream().map(entry -> new BoundaryStatistics.BoundaryTransactionCountPair(transactionD2BConverter.process(entry.getKey()).orElseThrow(), entry.getValue())).toList(),
                    mostProfitableMonth.getKey(),
                    mostProfitableMonth.getValue(),
                    mostUnprofitableMonth.getKey(),
                    mostUnprofitableMonth.getValue(),
                    averageTrend,
                    monthlyProfit.entrySet().stream()
                            .map(entry -> new BoundaryStatistics.BoundaryMonthByAmount(entry.getKey(), entry.getValue(), mapMonthTextToNumber(entry.getKey())))
                            .sorted(Comparator.comparing(BoundaryStatistics.BoundaryMonthByAmount::sortIndex))
                            .collect(Collectors.toUnmodifiableList()),
                    transactionD2BConverter.process(currentYearTransactions)
            );
        });
    }

    private String mapMonthToText(int input) {
        return switch (input) {
            case 1 -> "Sausis";
            case 2 -> "Vasaris";
            case 3 -> "Kovas";
            case 4 -> "Balandis";
            case 5 -> "Gegužė";
            case 6 -> "Birželis";
            case 7 -> "Liepa";
            case 8 -> "Rugpjūtis";
            case 9 -> "Rugsėjis";
            case 10 -> "Spalis";
            case 11 -> "Lapkritis";
            case 12 -> "Gruodis";
            default -> "";
        };
    }

    private int mapMonthTextToNumber(String input) {
        return switch (input) {
            case "Sausis" -> 1;
            case "Vasaris" -> 2;
            case "Kovas" -> 3;
            case "Balandis" -> 4;
            case "Gegužė" -> 5;
            case "Birželis" -> 6;
            case "Liepa" -> 7;
            case "Rugpjūtis" -> 8;
            case "Rugsėjis" -> 9;
            case "Spalis" -> 10;
            case "Lapkritis" -> 11;
            case "Gruodis" -> 12;
            default -> 69;
        };
    }
}
