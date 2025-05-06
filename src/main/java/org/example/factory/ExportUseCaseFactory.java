package org.example.factory;

import org.example.usecase.ExportOrgUnitTransactionsUseCase;

public interface ExportUseCaseFactory {
    ExportOrgUnitTransactionsUseCase createExportOrgUnitTransactionsUseCase();
}
