package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.domain.Transaction;
import org.example.usecase.RetrieveTransactionsUseCase;
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
public class RetrieveTransactionsInteractorTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private TransactionD2BConverter transactionD2BConverter;

    private RetrieveTransactionsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }

    @Test
    void executeShouldReturnTransactionsWhenFound() {
        // Prepare test data for Transaction (domain model)
        Transaction transaction1 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 100.0, "Transaction 1", LocalDate.of(2025, 5, 10), false);
        Transaction transaction2 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 200.0, "Transaction 2", LocalDate.of(2025, 5, 12), true);

        List<Transaction> transactions = List.of(transaction1, transaction2);

        // Prepare expected BoundaryTransaction (boundary model)
        BoundaryTransaction boundaryTransaction1 = new BoundaryTransaction(transaction1.id(), transaction1.orgUnitId(), transaction1.userId(), transaction1.amount(), transaction1.title(), transaction1.createdAt(), transaction1.isLocked());
        BoundaryTransaction boundaryTransaction2 = new BoundaryTransaction(transaction2.id(), transaction2.orgUnitId(), transaction2.userId(), transaction2.amount(), transaction2.title(), transaction2.createdAt(), transaction2.isLocked());

        List<BoundaryTransaction> boundaryTransactions = List.of(boundaryTransaction1, boundaryTransaction2);

        // Mock behavior
        when(transactionGateway.retrieve()).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(transactionGateway).retrieve();
        verify(transactionD2BConverter).process(transactions);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoTransactionsFound() {
        // Prepare test data for an empty list of transactions
        List<Transaction> transactions = List.of();
        List<BoundaryTransaction> boundaryTransactions = List.of();

        // Mock behavior
        when(transactionGateway.retrieve()).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(transactionGateway).retrieve();
        verify(transactionD2BConverter).process(transactions);
    }

    @Test
    void executeShouldReturnConvertedTransactions() {
        // Prepare test data for Transaction (domain model)
        Transaction transaction1 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 100.0, "Transaction 1", LocalDate.of(2025, 5, 10), false);

        List<Transaction> transactions = List.of(transaction1);

        // Prepare expected BoundaryTransaction (boundary model)
        BoundaryTransaction boundaryTransaction1 = new BoundaryTransaction(transaction1.id(), transaction1.orgUnitId(), transaction1.userId(), transaction1.amount(), transaction1.title(), transaction1.createdAt(), transaction1.isLocked());

        List<BoundaryTransaction> boundaryTransactions = List.of(boundaryTransaction1);

        // Mock behavior
        when(transactionGateway.retrieve()).thenReturn(transactions);
        when(transactionD2BConverter.process(transactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions - check if the transaction is converted correctly
        assertThat(result.get(0).id()).isEqualTo(boundaryTransaction1.id());
        assertThat(result.get(0).orgUnitId()).isEqualTo(boundaryTransaction1.orgUnitId());
        assertThat(result.get(0).userId()).isEqualTo(boundaryTransaction1.userId());

        // Verify interactions with mocks
        verify(transactionGateway).retrieve();
        verify(transactionD2BConverter).process(transactions);
    }
}
