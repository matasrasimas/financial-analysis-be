package org.example.converter;

import org.example.model.boundary.BoundaryTransactionFromFile;
import org.example.model.rest.RestTransactionFromFile;

public class TransactionFromFileB2RConverter extends Converter<BoundaryTransactionFromFile, RestTransactionFromFile> {
    @Override
    protected RestTransactionFromFile convert(BoundaryTransactionFromFile input) {
        return new RestTransactionFromFile(
                input.amount().orElse(null),
                input.title().orElse(null),
                input.description().orElse(null),
                input.createdAt().orElse(null)
        );
    }
}
