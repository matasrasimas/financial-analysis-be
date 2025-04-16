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
                input.amount(),
                input.title(),
                Optional.ofNullable(input.description()),
                LocalDate.parse(input.createdAt())
        );
    }
}
