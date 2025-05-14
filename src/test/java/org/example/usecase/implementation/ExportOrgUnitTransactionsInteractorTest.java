package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import org.example.gateway.ExportGateway;
import org.example.gateway.TransactionGateway;
import org.example.model.boundary.BoundaryFile;
import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionsReportRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportOrgUnitTransactionsInteractorTest {

    @Mock
    private ExportGateway exportGateway;

    @Mock
    private TransactionGateway transactionGateway;

    private TestScheduler testScheduler;
    private TestObserver<BoundaryFile> testObserver;

    private ExportOrgUnitTransactionsInteractor interactor;

    @BeforeEach
    void setUp() {
        testScheduler = new TestScheduler();
        testObserver = new TestObserver<>();
        interactor = new ExportOrgUnitTransactionsInteractor(testScheduler, exportGateway, transactionGateway);
    }

    @Test
    void executeReturnsExportedBoundaryFile() {
        UUID orgUnitId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        // Sample transactions
        Transaction t1 = new Transaction(UUID.randomUUID(), orgUnitId, UUID.randomUUID(), 150.0, "Purchase A", LocalDate.of(2024, 3, 10), false);
        Transaction t2 = new Transaction(UUID.randomUUID(), orgUnitId, UUID.randomUUID(), 50.0, "Purchase B", LocalDate.of(2024, 1, 5), false);

        when(transactionGateway.retrieveByOrgUnitId(orgUnitId, from, to)).thenReturn(List.of(t1, t2));
        when(exportGateway.exportTransactions(any(), any(), anyDouble())).thenReturn(new ByteArrayOutputStream());

        interactor.execute(orgUnitId.toString(), from, to)
                        .subscribeOn(testScheduler)
                                .subscribe(testObserver);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertValue(output -> {
            assertThat(output).isNotNull();
            assertThat(output.getName()).contains("Transakciju ataskaita");
            assertThat(output.getContent()).isInstanceOf(ByteArrayOutputStream.class);
            return true;
        });


        verify(transactionGateway).retrieveByOrgUnitId(orgUnitId, from, to);
        verify(exportGateway).exportTransactions(anyString(), anyList(), eq(200.0));
    }

    @Test
    void executeHandlesEmptyTransactionsGracefully() {
        UUID orgUnitId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        when(transactionGateway.retrieveByOrgUnitId(orgUnitId, from, to)).thenReturn(List.of());
        when(exportGateway.exportTransactions(any(), any(), eq(0.0))).thenReturn(new ByteArrayOutputStream());

        interactor.execute(orgUnitId.toString(), from, to)
                .subscribeOn(testScheduler)
                .subscribe(testObserver);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertValue(output -> {
            assertThat(output).isNotNull();
            assertThat(output.getName()).contains("Transakciju ataskaita");
            return true;
        });



        verify(transactionGateway).retrieveByOrgUnitId(orgUnitId, from, to);
        verify(exportGateway).exportTransactions(anyString(), eq(List.of()), eq(0.0));
    }

    @Test
    void executeShouldFailIfExportGatewayThrows() {
        UUID orgUnitId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        when(transactionGateway.retrieveByOrgUnitId(orgUnitId, from, to)).thenReturn(List.of());
        when(exportGateway.exportTransactions(any(), any(), anyDouble()))
                .thenThrow(new RuntimeException("Export failed"));

        interactor.execute(orgUnitId.toString(), from, to)
                .subscribeOn(testScheduler)
                .subscribe(testObserver);

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        testObserver.assertError(RuntimeException.class);
    }

    @Test
    void executeShouldHandleNullTransactionListGracefully() {
        UUID orgUnitId = UUID.randomUUID();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        when(transactionGateway.retrieveByOrgUnitId(orgUnitId, from, to)).thenReturn(null);

        interactor.execute(orgUnitId.toString(), from, to)
                .subscribeOn(testScheduler)
                .subscribe(testObserver);

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        testObserver.assertError(NullPointerException.class); // Or custom exception if your logic guards this
    }
}
