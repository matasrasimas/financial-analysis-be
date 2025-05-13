package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;
import org.example.usecase.RetrieveOrgUnitAutomaticTransactionsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RetrieveOrgUnitAutomaticTransactionsInteractorTest {

    @Mock
    private AutomaticTransactionGateway automaticTransactionGateway;

    @Mock
    private AutomaticTransactionD2BConverter automaticTransactionD2BConverter;

    private RetrieveOrgUnitAutomaticTransactionsUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveOrgUnitAutomaticTransactionsInteractor(automaticTransactionGateway, automaticTransactionD2BConverter);
    }

    @Test
    void executeShouldReturnConvertedAutomaticTransactions() {
        // Prepare test data for AutomaticTransaction (domain model)
        UUID orgUnitId = UUID.randomUUID();

        // AutomaticTransaction 1
        AutomaticTransaction automaticTransaction1 = new AutomaticTransaction(
                UUID.randomUUID(), orgUnitId, 100.0, "Auto Transaction 1", Optional.of("Description 1"),
                30, AutomaticTransaction.DurationUnit.MINUTES, "2023-06-15");

        // AutomaticTransaction 2
        AutomaticTransaction automaticTransaction2 = new AutomaticTransaction(
                UUID.randomUUID(), orgUnitId, 200.0, "Auto Transaction 2", Optional.empty(),
                60, AutomaticTransaction.DurationUnit.HOURS, "2023-06-16");

        List<AutomaticTransaction> domainTransactions = List.of(automaticTransaction1, automaticTransaction2);

        // Prepare expected BoundaryAutomaticTransaction (boundary model)
        BoundaryAutomaticTransaction boundaryAutomaticTransaction1 = new BoundaryAutomaticTransaction(
                automaticTransaction1.id(), automaticTransaction1.orgUnitId(), automaticTransaction1.amount(),
                automaticTransaction1.title(), automaticTransaction1.description(), automaticTransaction1.duration(),
                automaticTransaction1.durationUnit().name(), automaticTransaction1.nextTransactionDate());

        BoundaryAutomaticTransaction boundaryAutomaticTransaction2 = new BoundaryAutomaticTransaction(
                automaticTransaction2.id(), automaticTransaction2.orgUnitId(), automaticTransaction2.amount(),
                automaticTransaction2.title(), automaticTransaction2.description(), automaticTransaction2.duration(),
                automaticTransaction2.durationUnit().name(), automaticTransaction2.nextTransactionDate());

        List<BoundaryAutomaticTransaction> boundaryTransactions = List.of(boundaryAutomaticTransaction1, boundaryAutomaticTransaction2);

        // Mock behavior
        when(automaticTransactionGateway.retrieveByOrgUnitId(orgUnitId)).thenReturn(domainTransactions);
        when(automaticTransactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryAutomaticTransaction> result = interactor.execute(orgUnitId).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boundaryTransactions);

        // Verify interactions with mocks
        verify(automaticTransactionGateway).retrieveByOrgUnitId(orgUnitId);
        verify(automaticTransactionD2BConverter).process(domainTransactions);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoTransactionsFound() {
        // Prepare empty test data
        UUID orgUnitId = UUID.randomUUID();

        List<AutomaticTransaction> domainTransactions = List.of();
        List<BoundaryAutomaticTransaction> boundaryTransactions = List.of();

        // Mock behavior
        when(automaticTransactionGateway.retrieveByOrgUnitId(orgUnitId)).thenReturn(domainTransactions);
        when(automaticTransactionD2BConverter.process(domainTransactions)).thenReturn(boundaryTransactions);

        // Execute the use case
        List<BoundaryAutomaticTransaction> result = interactor.execute(orgUnitId).test()
                .assertComplete()
                .assertNoErrors()
                .values()
                .get(0);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(automaticTransactionGateway).retrieveByOrgUnitId(orgUnitId);
        verify(automaticTransactionD2BConverter).process(domainTransactions);
    }
}
