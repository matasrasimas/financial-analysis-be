package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.enums.FileType;
import org.example.model.boundary.BoundaryTransactionFromFile;

import java.util.List;

public interface GenerateTransactionsFromFileUseCase {
    Single<List<BoundaryTransactionFromFile>> execute(byte[] fileBytes);
}
