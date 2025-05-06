package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RestStatistics(
        @JsonProperty("from") String from,
        @JsonProperty("to") String to,
        @JsonProperty("xValues") List<String> xValues,
        @JsonProperty("previousYearYValues") List<Double> previousYearYValues,
        @JsonProperty("currentYearYValues") List<Double> currentYearYValues,
        @JsonProperty("currentYearNumberOfTransactions") int currentYearNumberOfTransactions,
        @JsonProperty("previousYearNumberOfTransactions") int previousYearNumberOfTransactions,
        @JsonProperty("goalCompletionPercentage") double goalCompletionPercentage,
        @JsonProperty("currentYearTotalAmount") double currentYearTotalAmount,
        @JsonProperty("previousYearTotalAmount") double previousYearTotalAmount,
        @JsonProperty("top5MostFrequentTransactions") List<RestTransactionCountPair> top5MostFrequentTransactions,
        @JsonProperty("mostProfitableMonth") String mostProfitableMonth,
        @JsonProperty("mostProfitableMonthAmount") double mostProfitableMonthAmount,
        @JsonProperty("mostUnprofitableMonth") String mostUnprofitableMonth,
        @JsonProperty("mostUnprofitableMonthAmount") double mostUnprofitableMonthAmount,
        @JsonProperty("averageTrendPercentage") double averageTrendPercentage,
        @JsonProperty("monthsByAmounts") List<RestMonthByAmount> monthsByAmounts,
        @JsonProperty("currentYearTransactions") List<RestTransaction> currentYearTransactions) {

    public record RestTransactionCountPair(
            @JsonProperty("transaction") RestTransaction transaction,
            @JsonProperty("count") long count) {}

    public record RestMonthByAmount(
            @JsonProperty("month") String month,
            @JsonProperty("amount") double amount,
            @JsonProperty("sortIndex") int sortIndex) {}

}
