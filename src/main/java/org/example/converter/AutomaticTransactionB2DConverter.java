package org.example.converter;

import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;

public class AutomaticTransactionB2DConverter extends  Converter<BoundaryAutomaticTransaction, AutomaticTransaction> {
    @Override
    protected AutomaticTransaction convert(BoundaryAutomaticTransaction input) {
        return new AutomaticTransaction(
                input.id(),
                input.amount(),
                input.title(),
                input.description(),
                input.latestTransactionDate(),
                input.durationMinutes(),
                AutomaticTransaction.DurationUnit.valueOf(input.durationUnit())
        );
    }
}
