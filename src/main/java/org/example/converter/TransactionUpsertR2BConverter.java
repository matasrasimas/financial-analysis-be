package org.example.converter;

import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.rest.RestTransactionUpsert;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class TransactionUpsertR2BConverter extends Converter<RestTransactionUpsert, BoundaryTransactionUpsert> {
    @Override
    protected BoundaryTransactionUpsert convert(RestTransactionUpsert input) {
        return new BoundaryTransactionUpsert(
                Optional.ofNullable(input.id()).map(UUID::fromString).orElse(UUID.randomUUID()),
                UUID.fromString(input.orgUnitId()),
                UUID.fromString(input.userId()),
                input.amount(),
                input.title(),
                LocalDate.parse(input.createdAt()),
                input.isLocked()
        );
    }
}
