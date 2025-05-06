package org.example.converter;

import org.example.model.boundary.BoundaryTransaction;
import org.example.model.rest.RestTransaction;

import java.util.Optional;
import java.util.UUID;

public class TransactionB2RConverter extends Converter<BoundaryTransaction, RestTransaction> {
    @Override
    protected RestTransaction convert(BoundaryTransaction input) {
        return new RestTransaction(
                input.id().toString(),
                input.orgUnitId().toString(),
                Optional.ofNullable(input.userId()).map(UUID::toString).orElse(null),
                input.amount(),
                input.title(),
                input.createdAt().toString(),
                input.isLocked()
        );
    }
}
