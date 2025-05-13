package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.AutomaticTransactionB2DConverter;
import org.example.converter.AutomaticTransactionD2BConverter;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.domain.AutomaticTransaction;
import org.example.model.domain.AutomaticTransaction.DurationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UpsertAutomaticTransactionInteractorTest {

    @Mock
    private AutomaticTransactionGateway gateway;

    @Mock
    private AutomaticTransactionB2DConverter b2dConverter;

    @Mock
    private AutomaticTransactionD2BConverter d2bConverter;

    private UpsertAutomaticTransactionInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new UpsertAutomaticTransactionInteractor(gateway, b2dConverter, d2bConverter);
    }

    @Test
    void execute_shouldConvertBoundaryToDomainUpsertAndReturnConvertedResult() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orgUnitId = UUID.randomUUID();

        BoundaryAutomaticTransaction inputBoundary = new BoundaryAutomaticTransaction(
                id, orgUnitId, 100.0, "Rent", Optional.of("Monthly payment"),
                30, "DAYS", "2025-06-01"
        );

        AutomaticTransaction domainTransaction = new AutomaticTransaction(
                id, orgUnitId, 100.0, "Rent", Optional.of("Monthly payment"),
                30, DurationUnit.DAYS, "2025-06-01"
        );

        BoundaryAutomaticTransaction outputBoundary = new BoundaryAutomaticTransaction(
                id, orgUnitId, 100.0, "Rent", Optional.of("Monthly payment"),
                30, "DAYS", "2025-06-01"
        );

        when(b2dConverter.process(inputBoundary)).thenReturn(Optional.of(domainTransaction));
        when(gateway.upsert(domainTransaction)).thenReturn(domainTransaction);
        when(d2bConverter.process(domainTransaction)).thenReturn(Optional.of(outputBoundary));

        // Act
        Single<BoundaryAutomaticTransaction> result = interactor.execute(inputBoundary);

        // Assert
        BoundaryAutomaticTransaction finalResult = result.blockingGet();
        assertThat(finalResult).isEqualTo(outputBoundary);

        verify(b2dConverter).process(inputBoundary);
        verify(gateway).upsert(domainTransaction);
        verify(d2bConverter).process(domainTransaction);
    }
}
