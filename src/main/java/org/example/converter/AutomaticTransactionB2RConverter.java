package org.example.converter;

import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.rest.RestAutomaticTransaction;

public class AutomaticTransactionB2RConverter extends Converter<BoundaryAutomaticTransaction, RestAutomaticTransaction> {
    @Override
    protected RestAutomaticTransaction convert(BoundaryAutomaticTransaction input) {
        return new RestAutomaticTransaction(
                input.id().toString(),
                input.amount(),
                input.title(),
                input.description().orElse(null),
                input.latestTransactionDate(),
                input.durationMinutes(),
                input.durationUnit()


        );
    }
}
