package org.example.converter;

import org.example.model.boundary.BoundaryTransaction;
import org.example.model.domain.Transaction;

public class TransactionD2BConverter extends Converter<Transaction, BoundaryTransaction> {
    @Override
    protected BoundaryTransaction convert(Transaction input) {
        return new BoundaryTransaction(
                input.id(),
                input.orgUnitId(),
                input.amount(),
                input.title(),
                input.description(),
                input.createdAt()
        );
    }
}
