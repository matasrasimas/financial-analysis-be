package org.example.converter;

import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;

public class AutomaticTransactionD2BConverter extends Converter<AutomaticTransaction, BoundaryAutomaticTransaction> {
    @Override
    protected BoundaryAutomaticTransaction convert(AutomaticTransaction input) {
        return new BoundaryAutomaticTransaction(
                input.id(),
                input.orgUnitId(),
                input.amount(),
                input.title(),
                input.description(),
                input.duration(),
                input.durationUnit().toString(),
                input.nextTransactionDate()
        );
    }
}
