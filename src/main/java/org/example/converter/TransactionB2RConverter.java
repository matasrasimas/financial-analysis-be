package org.example.converter;

import org.example.model.boundary.BoundaryTransaction;
import org.example.model.rest.RestTransaction;

public class TransactionB2RConverter extends Converter<BoundaryTransaction, RestTransaction> {
    @Override
    protected RestTransaction convert(BoundaryTransaction input) {
        return new RestTransaction(
                input.id().toString(),
                input.amount(),
                input.title(),
                input.description().orElse(null),
                input.createdAt()
        );
    }
}
