package org.example.usecase.implementation;

import org.example.converter.UserCreateB2DConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserCreate;
import org.example.model.domain.UserCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserInteractorTest {

    @Mock
    private UserGateway userGateway;
    @Mock
    private UserCreateB2DConverter userCreateB2DConverter;

    private CreateUserInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new CreateUserInteractor(userGateway, userCreateB2DConverter);
    }

    @Test
    void executeSuccessfullyCreatesUser() {
        BoundaryUserCreate input = mock(BoundaryUserCreate.class);
        UserCreate domainUser = mock(UserCreate.class);

        when(userCreateB2DConverter.process(input)).thenReturn(Optional.of(domainUser));

        interactor.execute(input);

        verify(userCreateB2DConverter).process(input);
        verify(userGateway).create(domainUser);
    }

    @Test
    void executeThrowsWhenConversionFails() {
        BoundaryUserCreate input = mock(BoundaryUserCreate.class);

        when(userCreateB2DConverter.process(input)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> interactor.execute(input));

        verify(userCreateB2DConverter).process(input);
        verifyNoInteractions(userGateway);
    }
}