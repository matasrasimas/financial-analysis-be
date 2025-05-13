package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.UserDTOB2DConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;
import org.example.usecase.UpdateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UpdateUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserDTOB2DConverter userDTOB2DConverter;

    private UpdateUserUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new UpdateUserInteractor(userGateway, userDTOB2DConverter);
    }

    @Test
    void executeShouldUpdateUserSuccessfully() {
        // Prepare test data
        UUID userId = UUID.randomUUID();
        BoundaryUserDTO boundaryUserDTO = new BoundaryUserDTO(
                userId, "John", "Doe", Optional.of("123456789"), Optional.of("john.doe@example.com"));

        UserDTO domainUserDTO = new UserDTO(
                userId, "John", "Doe", Optional.of("123456789"), Optional.of("john.doe@example.com"));

        // Mock behavior
        when(userDTOB2DConverter.process(boundaryUserDTO)).thenReturn(Optional.of(domainUserDTO));

        // Execute the use case
        Completable result = interactor.execute(boundaryUserDTO);

        // Assertions
        result.test().assertComplete();

        // Verify interactions with mocks
        verify(userDTOB2DConverter).process(boundaryUserDTO);
        verify(userGateway).update(domainUserDTO);
    }

    @Test
    void executeShouldThrowExceptionIfConversionFails() {
        // Prepare test data
        UUID userId = UUID.randomUUID();
        BoundaryUserDTO boundaryUserDTO = new BoundaryUserDTO(
                userId, "John", "Doe", Optional.of("123456789"), Optional.of("john.doe@example.com"));

        // Mock behavior
        when(userDTOB2DConverter.process(boundaryUserDTO)).thenReturn(Optional.empty());

        // Execute the use case and verify that an exception is thrown
        interactor.execute(boundaryUserDTO)
        .test().assertError(e -> e.getClass().equals(NoSuchElementException.class));

        // Verify interactions with mocks
        verify(userDTOB2DConverter).process(boundaryUserDTO);
        verify(userGateway, never()).update(any()); // Ensure update was not called
    }

    @Test
    void executeShouldCallUpdateOnGatewayWhenConversionIsSuccessful() {
        // Prepare test data
        UUID userId = UUID.randomUUID();
        BoundaryUserDTO boundaryUserDTO = new BoundaryUserDTO(
                userId, "Jane", "Smith", Optional.of("987654321"), Optional.of("jane.smith@example.com"));

        UserDTO domainUserDTO = new UserDTO(
                userId, "Jane", "Smith", Optional.of("987654321"), Optional.of("jane.smith@example.com"));

        // Mock behavior
        when(userDTOB2DConverter.process(boundaryUserDTO)).thenReturn(Optional.of(domainUserDTO));

        // Execute the use case
        Completable result = interactor.execute(boundaryUserDTO);

        // Assertions
        result.test().assertComplete();

        // Verify that the correct method was called
        verify(userDTOB2DConverter).process(boundaryUserDTO);
        verify(userGateway).update(domainUserDTO);
    }
}
