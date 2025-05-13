package org.example.usecase.implementation;

import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;
import org.example.usecase.RetrieveAutomaticTransactionsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetrieveAutomaticTransactionsInteractorTest {

    @Mock
    private AutomaticTransactionGateway automaticTransactionGateway;

    @Mock
    private AutomaticTransactionD2BConverter automaticTransactionD2BConverter;

    private RetrieveAutomaticTransactionsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveAutomaticTransactionsInteractor(automaticTransactionGateway, automaticTransactionD2BConverter);
    }

    @Test
    void executeShouldReturnBoundaryTransactions() {
        // Prepare test data for AutomaticTransaction
        UUID id = UUID.randomUUID();
        UUID orgUnitId = UUID.randomUUID();
        double amount = 100.50;
        String title = "Test Transaction";
        Optional<String> description = Optional.of("This is a test transaction");
        int duration = 30;
        String durationUnit = "DAYS";  // As a String (from your Boundary model)
        String nextTransactionDate = "2025-05-14";

        AutomaticTransaction domainTransaction = new AutomaticTransaction(id, orgUnitId, amount, title, description, duration, AutomaticTransaction.DurationUnit.DAYS, nextTransactionDate);
        List<AutomaticTransaction> domainTransactions = List.of(domainTransaction);

        BoundaryAutomaticTransaction boundaryTransaction = new BoundaryAutomaticTransaction(id, orgUnitId, amount, title, description, duration, durationUnit, nextTransactionDate);
        List<BoundaryAutomaticTransaction> boundaryTransactions = List.of(boundaryTransaction);

        // Mock behavior
        when(automaticTransactionGateway.retrieve()).thenReturn(domainTransactions);
        when(automaticTransactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryAutomaticTransaction> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(automaticTransactionGateway).retrieve();
        verify(automaticTransactionD2BConverter).process(domainTransactions);
    }

    @Test
    void executeShouldHandleEmptyTransactions() {
        // Prepare test data
        List<AutomaticTransaction> domainTransactions = List.of();
        List<BoundaryAutomaticTransaction> boundaryTransactions = List.of();

        // Mock behavior
        when(automaticTransactionGateway.retrieve()).thenReturn(domainTransactions);
        when(automaticTransactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryAutomaticTransaction> result = interactor.execute().test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(automaticTransactionGateway).retrieve();
        verify(automaticTransactionD2BConverter).process(domainTransactions);
    }
}
