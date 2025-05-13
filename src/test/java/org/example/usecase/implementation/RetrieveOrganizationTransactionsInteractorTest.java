package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionD2BConverter;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryTransaction;
import org.example.model.domain.Transaction;
import org.example.usecase.RetrieveOrganizationTransactionsUseCase;
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
public class RetrieveOrganizationTransactionsInteractorTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private TransactionD2BConverter transactionD2BConverter;

    private RetrieveOrganizationTransactionsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrganizationTransactionsInteractor(transactionGateway, transactionD2BConverter);
    }

    @Test
    void executeShouldReturnSortedBoundaryTransactions() {
        // Prepare test data for Transaction (domain model)
        UUID orgId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        // Transaction 1
        Transaction transaction1 = new Transaction(
                UUID.randomUUID(), orgId, userId, 100.0, "Transaction 1", LocalDate.of(2023, 5, 1), false);

        // Transaction 2
        Transaction transaction2 = new Transaction(
                UUID.randomUUID(), orgId, userId, 200.0, "Transaction 2", LocalDate.of(2023, 3, 15), true);

        List<Transaction> domainTransactions = List.of(transaction1, transaction2);

        // Prepare expected BoundaryTransaction (boundary model)
        BoundaryTransaction boundaryTransaction1 = new BoundaryTransaction(
                transaction1.id(), transaction1.orgUnitId(), transaction1.userId(), transaction1.amount(),
                transaction1.title(), transaction1.createdAt(), transaction1.isLocked());

        BoundaryTransaction boundaryTransaction2 = new BoundaryTransaction(
                transaction2.id(), transaction2.orgUnitId(), transaction2.userId(), transaction2.amount(),
                transaction2.title(), transaction2.createdAt(), transaction2.isLocked());

        List<BoundaryTransaction> boundaryTransactions = List.of(boundaryTransaction2, boundaryTransaction1); // Sorted by createdAt

        // Mock behavior
        when(transactionGateway.retrieveByOrgIdAndDate(orgId, from, to)).thenReturn(domainTransactions);
        when(transactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute(orgId, from, to).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(transactionGateway).retrieveByOrgIdAndDate(orgId, from, to);
        verify(transactionD2BConverter).process(domainTransactions);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoTransactionsFound() {
        // Prepare empty test data
        UUID orgId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        List<Transaction> domainTransactions = List.of();
        List<BoundaryTransaction> boundaryTransactions = List.of();

        // Mock behavior
        when(transactionGateway.retrieveByOrgIdAndDate(orgId, from, to)).thenReturn(domainTransactions);
        when(transactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryTransaction> result = interactor.execute(orgId, from, to).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(transactionGateway).retrieveByOrgIdAndDate(orgId, from, to);
        verify(transactionD2BConverter).process(domainTransactions);
    }
}
