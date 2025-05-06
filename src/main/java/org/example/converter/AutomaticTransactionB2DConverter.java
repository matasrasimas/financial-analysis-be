package org.example.converter;

import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;

public class AutomaticTransactionB2DConverter extends  Converter<BoundaryAutomaticTransaction, AutomaticTransaction> {
    @Override
    protected AutomaticTransaction convert(BoundaryAutomaticTransaction input) {
        return new AutomaticTransaction(
                input.id(),
                input.orgUnitId(),
                input.amount(),
                input.title(),
                input.description(),
                input.duration(),
                AutomaticTransaction.DurationUnit.valueOf(input.durationUnit()),
                input.nextTransactionDate()
        );
    }
}
