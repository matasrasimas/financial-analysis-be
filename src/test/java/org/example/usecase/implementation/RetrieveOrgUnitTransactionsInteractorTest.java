package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.domain.Transaction;
import org.example.usecase.RetrieveOrgUnitTransactionsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RetrieveOrgUnitTransactionsInteractorTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private TransactionD2BConverter transactionD2BConverter;

    private RetrieveOrgUnitTransactionsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrgUnitTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }

    @Test
    void executeShouldReturnSortedTransactionsWhenFound() {
        // Prepare test data for Transaction (domain model)
        Transaction transaction1 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 100.0, "Title 1", LocalDate.of(2025, 5, 10), false);
        Transaction transaction2 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 200.0, "Title 2", LocalDate.of(2025, 5, 12), true);
        Transaction transaction3 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 50.0, "Title 3", LocalDate.of(2025, 5, 8), false);
        List<Transaction> transactions = List.of(transaction1, transaction2, transaction3);

        // Prepare expected BoundaryTransaction (boundary model)
        BoundaryTransaction boundaryTransaction1 = new BoundaryTransaction(transaction1.id(), transaction1.orgUnitId(), transaction1.userId(), transaction1.amount(), transaction1.title(), transaction1.createdAt(), transaction1.isLocked());
        BoundaryTransaction boundaryTransaction2 = new BoundaryTransaction(transaction2.id(), transaction2.orgUnitId(), transaction2.userId(), transaction2.amount(), transaction2.title(), transaction2.createdAt(), transaction2.isLocked());
        BoundaryTransaction boundaryTransaction3 = new BoundaryTransaction(transaction3.id(), transaction3.orgUnitId(), transaction3.userId(), transaction3.amount(), transaction3.title(), transaction3.createdAt(), transaction3.isLocked());

        List<BoundaryTransaction> boundaryTransactions = List.of(boundaryTransaction2, boundaryTransaction1, boundaryTransaction3);

        // Mock behavior
        when(transactionGateway.retrieveByOrgUnitId(any(), any(), any())).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute(UUID.randomUUID(), LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 15)).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(transactionGateway).retrieveByOrgUnitId(any(), any(), any());
        verify(transactionD2BConverter).process(transactions);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoTransactionsFound() {
        // Prepare test data for an empty list of transactions
        List<Transaction> transactions = List.of();
        List<BoundaryTransaction> boundaryTransactions = List.of();

        // Mock behavior
        when(transactionGateway.retrieveByOrgUnitId(any(), any(), any())).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute(UUID.randomUUID(), LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 15)).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(transactionGateway).retrieveByOrgUnitId(any(), any(), any());
        verify(transactionD2BConverter).process(transactions);
    }

    @Test
    void executeShouldReturnSortedTransactionsInReverseOrder() {
        // Prepare test data for Transaction (domain model)
        Transaction transaction1 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 100.0, "Title 1", LocalDate.of(2025, 5, 10), false);
        Transaction transaction2 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 200.0, "Title 2", LocalDate.of(2025, 5, 12), true);
        Transaction transaction3 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 50.0, "Title 3", LocalDate.of(2025, 5, 8), false);

        List<Transaction> transactions = List.of(transaction1, transaction2, transaction3);

        // Prepare expected BoundaryTransaction (boundary model)
        BoundaryTransaction boundaryTransaction1 = new BoundaryTransaction(transaction1.id(), transaction1.orgUnitId(), transaction1.userId(), transaction1.amount(), transaction1.title(), transaction1.createdAt(), transaction1.isLocked());
        BoundaryTransaction boundaryTransaction2 = new BoundaryTransaction(transaction2.id(), transaction2.orgUnitId(), transaction2.userId(), transaction2.amount(), transaction2.title(), transaction2.createdAt(), transaction2.isLocked());
        BoundaryTransaction boundaryTransaction3 = new BoundaryTransaction(transaction3.id(), transaction3.orgUnitId(), transaction3.userId(), transaction3.amount(), transaction3.title(), transaction3.createdAt(), transaction3.isLocked());

        List<BoundaryTransaction> boundaryTransactions = List.of(boundaryTransaction2, boundaryTransaction1, boundaryTransaction3);

        // Mock behavior
        when(transactionGateway.retrieveByOrgUnitId(any(), any(), any())).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute(UUID.randomUUID(), LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 15)).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions - check if the list is sorted in reverse order
        assertThat(result).isEqualTo(boundaryTransactions);
        assertThat(result.get(0).createdAt()).isEqualTo(LocalDate.of(2025, 5, 12));
        assertThat(result.get(1).createdAt()).isEqualTo(LocalDate.of(2025, 5, 10));
        assertThat(result.get(2).createdAt()).isEqualTo(LocalDate.of(2025, 5, 8));

        // Verify interactions with mocks
        verify(transactionGateway).retrieveByOrgUnitId(any(), any(), any());
        verify(transactionD2BConverter).process(transactions);
    }
}
