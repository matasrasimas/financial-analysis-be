package org.example.converter;

import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.domain.TransactionUpsert;

public class TransactionUpsertB2DConverter extends Converter<BoundaryTransactionUpsert, TransactionUpsert> {
    @Override
    protected TransactionUpsert convert(BoundaryTransactionUpsert input) {
        return new TransactionUpsert(
                input.id(),
                input.orgUnitId(),
                input.userId(),
                input.amount(),
                input.title(),
                input.createdAt(),
                input.isLocked()
        );
    }
}
