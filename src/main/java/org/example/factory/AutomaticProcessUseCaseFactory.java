package org.example.factory;

import org.example.usecase.ProcessAutomaticTransactionsUseCase;

public interface AutomaticProcessUseCaseFactory {
    ProcessAutomaticTransactionsUseCase createProcessAutomaticTransactionsUseCase();
}
