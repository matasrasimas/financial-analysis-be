package org.example.converter;

import org.example.model.boundary.BoundaryTransactionFromFile;
import org.example.model.domain.TransactionFromFile;

public class TransactionFromFileD2BConverter extends Converter<TransactionFromFile, BoundaryTransactionFromFile> {
    @Override
    protected BoundaryTransactionFromFile convert(TransactionFromFile input) {
        return new BoundaryTransactionFromFile(
                input.amount(),
                input.title(),
                input.description(),
                input.createdAt()
        );
    }
}
