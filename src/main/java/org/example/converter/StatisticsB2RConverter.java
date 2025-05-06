package org.example.converter;

import org.example.model.boundary.BoundaryStatistics;
import org.example.model.rest.RestStatistics;

import java.util.stream.Collectors;

public class StatisticsB2RConverter extends Converter<BoundaryStatistics, RestStatistics> {
    private final TransactionB2RConverter transactionB2RConverter;

    public StatisticsB2RConverter(TransactionB2RConverter transactionB2RConverter) {
        this.transactionB2RConverter = transactionB2RConverter;
    }

    @Override
    protected RestStatistics convert(BoundaryStatistics input) {
        return new RestStatistics(
                input.from().toString(),
                input.to().toString(),
                input.xValues(),
                input.previousYearYValues(),
                input.currentYearYValues(),
                input.currentYearNumberOfTransactions(),
                input.previousYearNumberOfTransactions(),
                input.goalCompletionPercentage(),
                input.currentYearTotalAmount(),
                input.previousYearTotalAmount(),
                input.top5MostFrequentTransactions().stream()
                        .map(t -> new RestStatistics.RestTransactionCountPair(transactionB2RConverter.process(t.transaction()).orElseThrow(), t.count()))
                        .collect(Collectors.toUnmodifiableList()),
                input.mostProfitableMonth(),
                input.mostProfitableMonthAmount(),
                input.mostUnprofitableMonth(),
                input.mostUnprofitableMonthAmount(),
                input.averageTrendPercentage(),
                input.monthsByAmounts().stream()
                        .map(mba -> new RestStatistics.RestMonthByAmount(mba.month(), mba.amount(), mba.sortIndex()))
                        .collect(Collectors.toList()),
                transactionB2RConverter.process(input.currentYearTransactions())
        );
    }
}
