package org.example.usecase.implementation;

import org.example.converter.UserDTOD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.exception.SerializationException;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;
import org.example.usecase.TokenValidator;
import org.example.usecase.VerificationInteractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerificationInteractorTest {

    private UserGateway userGateway;
    private UserDTOD2BConverter userDTOD2BConverter;
    private TokenValidator tokenValidator;
    private VerificationInteractor interactor;

    @BeforeEach
    void setUp() {
        userGateway = mock(UserGateway.class);
        userDTOD2BConverter = mock(UserDTOD2BConverter.class);
        tokenValidator = mock(TokenValidator.class);
        interactor = new VerificationInteractor(userGateway, userDTOD2BConverter, tokenValidator);
    }

    @Test
    void verify_shouldReturnBoundaryUserDTO_whenTokenIsValidAndUserExists() {
        // Arrange
        String fakeToken = "valid.jwt.token";
        UUID userId = UUID.randomUUID();

        UserDTO user = new UserDTO(userId, "John", "Doe", Optional.empty(), Optional.of("john@example.com"));
        BoundaryUserDTO expected = new BoundaryUserDTO(userId, "John", "Doe", Optional.empty(), Optional.of("john@example.com"));

        when(tokenValidator.getUserIdFromJwt(fakeToken)).thenReturn(Optional.of(userId.toString()));
        when(userGateway.retrieveById(userId)).thenReturn(Optional.of(user));
        when(userDTOD2BConverter.process(user)).thenReturn(Optional.of(expected));

        // Act
        BoundaryUserDTO result = interactor.verify(fakeToken);

        // Assert
        assertEquals(expected, result);

        verify(tokenValidator).getUserIdFromJwt(fakeToken);
        verify(userGateway).retrieveById(userId);
        verify(userDTOD2BConverter).process(user);
    }
}
