package org.example.converter;

import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.rest.RestTransactionUpsert;

import java.util.Optional;
import java.util.UUID;

public class TransactionUpsertR2BConverter extends Converter<RestTransactionUpsert, BoundaryTransactionUpsert> {
    @Override
    protected BoundaryTransactionUpsert convert(RestTransactionUpsert input) {
        return new BoundaryTransactionUpsert(
                Optional.ofNullable(input.id()).map(UUID::fromString).orElse(UUID.randomUUID()),
                input.amount(),
                input.title(),
                Optional.ofNullable(input.description())
        );
    }
}
