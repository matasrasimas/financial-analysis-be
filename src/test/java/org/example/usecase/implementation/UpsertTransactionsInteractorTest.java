package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.converter.TransactionUpsertB2DConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.boundary.BoundaryTransactionUpsert;
import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionUpsert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UpsertTransactionsInteractorTest {

    private TransactionGateway transactionGateway;
    private TransactionUpsertB2DConverter transactionUpsertB2DConverter;
    private TransactionD2BConverter transactionD2BConverter;
    private UpsertTransactionsInteractor interactor;

    @BeforeEach
    void setUp() {
        transactionGateway = mock(TransactionGateway.class);
        transactionUpsertB2DConverter = mock(TransactionUpsertB2DConverter.class);
        transactionD2BConverter = mock(TransactionD2BConverter.class);
        interactor = new UpsertTransactionsInteractor(transactionGateway, transactionUpsertB2DConverter, transactionD2BConverter);
    }

    @Test
    void execute_shouldConvertAndUpsertSuccessfully() {
        // Arrange
        BoundaryTransactionUpsert input = new BoundaryTransactionUpsert(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                250.0,
                "Office Supplies",
                LocalDate.now(),
                false
        );

        TransactionUpsert domainUpsert = new TransactionUpsert(
                input.id(),
                input.orgUnitId(),
                input.userId(),
                input.amount(),
                input.title(),
                input.createdAt(),
                input.isLocked()
        );

        Transaction savedTransaction = new Transaction(
                input.id(),
                input.orgUnitId(),
                input.userId(),
                input.amount(),
                input.title(),
                input.createdAt(),
                input.isLocked()
        );

        BoundaryTransaction expectedBoundary = new BoundaryTransaction(
                savedTransaction.id(),
                savedTransaction.orgUnitId(),
                savedTransaction.userId(),
                savedTransaction.amount(),
                savedTransaction.title(),
                savedTransaction.createdAt(),
                savedTransaction.isLocked()
        );

        List<BoundaryTransactionUpsert> inputList = List.of(input);
        List<TransactionUpsert> upserts = List.of(domainUpsert);
        List<Transaction> saved = List.of(savedTransaction);

        when(transactionUpsertB2DConverter.process(inputList)).thenReturn(upserts);
        when(transactionGateway.upsert(upserts)).thenReturn(saved);
        when(transactionD2BConverter.process(savedTransaction)).thenReturn(Optional.of(expectedBoundary));

        // Act
        Single<BoundaryTransaction> result = interactor.execute(inputList);

        // Assert
        result.test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expectedBoundary);

        verify(transactionUpsertB2DConverter).process(inputList);
        verify(transactionGateway).upsert(upserts);
        verify(transactionD2BConverter).process(savedTransaction);
    }
}
