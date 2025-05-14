package org.example.usecase.implementation;

import org.example.exception.ItemNotFoundException;
import org.example.gateway.TransactionGateway;
import org.example.model.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTransactionInteractorTest {

    @Mock
    private TransactionGateway transactionGateway;

    private DeleteTransactionInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteTransactionInteractor(transactionGateway);
    }

    @Test
    void executeDeletesTransactionIfExists() {
        UUID transactionId = UUID.randomUUID();
        when(transactionGateway.retrieveById(transactionId)).thenReturn(Optional.of(mock(Transaction.class)));

        interactor.execute(transactionId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(transactionGateway).retrieveById(transactionId);
        verify(transactionGateway).delete(transactionId);
    }
}
