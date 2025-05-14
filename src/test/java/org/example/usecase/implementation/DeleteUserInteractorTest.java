package org.example.usecase.implementation;

import org.example.exception.ItemNotFoundException;
import org.example.gateway.UserGateway;
import org.example.model.domain.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    private DeleteUserInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new DeleteUserInteractor(userGateway);
    }

    @Test
    void executeDeletesUserIfExists() {
        UUID userId = UUID.randomUUID();
        when(userGateway.retrieveById(userId)).thenReturn(Optional.of(mock(UserDTO.class)));

        interactor.execute(userId)
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(userGateway).retrieveById(userId);
        verify(userGateway).delete(userId);
    }
}
