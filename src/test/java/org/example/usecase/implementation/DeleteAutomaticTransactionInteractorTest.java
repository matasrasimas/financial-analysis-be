package org.example.usecase.implementation;

import org.example.exception.ItemNotFoundException;
import org.example.gateway.AutomaticTransactionGateway;
import org.example.model.domain.AutomaticTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteAutomaticTransactionInteractorTest {

    @Mock
    private AutomaticTransactionGateway automaticTransactionGateway;

    private DeleteAutomaticTransactionInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteAutomaticTransactionInteractor(automaticTransactionGateway);
    }

    @Test
    void deleteExistingTransactionCompletes() {
        UUID transactionId = UUID.randomUUID();
        when(automaticTransactionGateway.retrieveById(transactionId)).thenReturn(Optional.of(mock(AutomaticTransaction.class)));

        interactor.execute(transactionId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(automaticTransactionGateway).retrieveById(transactionId);
        verify(automaticTransactionGateway).delete(transactionId);
    }

    @Test
    void deleteNonExistingTransactionThrowsItemNotFoundException() {
        UUID transactionId = UUID.randomUUID();
        when(automaticTransactionGateway.retrieveById(transactionId)).thenReturn(Optional.empty());

        interactor.execute(transactionId)
                .test()
                .assertError(e -> e.getClass().equals(ItemNotFoundException.class)
                        && e.getMessage().matches("automatic-transaction with id .* not found"));

        verify(automaticTransactionGateway).retrieveById(transactionId);
        verify(automaticTransactionGateway, never()).delete(any());
    }
}