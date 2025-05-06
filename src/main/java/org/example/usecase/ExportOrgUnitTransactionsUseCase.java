package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryFile;

import java.time.LocalDate;

public interface ExportOrgUnitTransactionsUseCase {
    Single<BoundaryFile> execute(String orgUnitId, LocalDate from, LocalDate to);
}
