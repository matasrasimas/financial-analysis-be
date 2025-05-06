package org.example.model.boundary;

import java.time.LocalDate;
import java.util.List;

public record BoundaryStatistics(LocalDate from,
                                 LocalDate to,
                                 List<String> xValues,
                                 List<Double> previousYearYValues,
                                 List<Double> currentYearYValues,
                                 int currentYearNumberOfTransactions,
                                 int previousYearNumberOfTransactions,
                                 double goalCompletionPercentage,
                                 double currentYearTotalAmount,
                                 double previousYearTotalAmount,
                                 List<BoundaryTransactionCountPair> top5MostFrequentTransactions,
                                 String mostProfitableMonth,
                                 double mostProfitableMonthAmount,
                                 String mostUnprofitableMonth,
                                 double mostUnprofitableMonthAmount,
                                 double averageTrendPercentage,
                                 List<BoundaryMonthByAmount> monthsByAmounts,
                                 List<BoundaryTransaction> currentYearTransactions) {

    public record BoundaryTransactionCountPair(BoundaryTransaction transaction, long count) {}

    public record BoundaryMonthByAmount(String month, double amount, int sortIndex) {}
}
