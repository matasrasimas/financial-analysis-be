package org.example.usecase.implementation;

import org.example.converter.UserDTOD2BConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;
import org.example.usecase.RetrieveUsersUseCase;
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
public class RetrieveUsersInteractorTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserDTOD2BConverter userDTOD2BConverter;

    private RetrieveUsersUseCase interactor;

    @BeforeEach
    void setUp() {
        interactor = new RetrieveUsersInteractor(userGateway, userDTOD2BConverter);
    }

    @Test
    void executeShouldReturnUsersWhenFound() {
        // Prepare test data for UserDTO (domain model)
        UserDTO user1 = new UserDTO(UUID.randomUUID(), "John", "Doe", Optional.of("123-456-7890"), Optional.of("john.doe@example.com"));
        UserDTO user2 = new UserDTO(UUID.randomUUID(), "Jane", "Smith", Optional.empty(), Optional.of("jane.smith@example.com"));

        List<UserDTO> users = List.of(user1, user2);

        // Prepare expected BoundaryUserDTO (boundary model)
        BoundaryUserDTO boundaryUser1 = new BoundaryUserDTO(user1.id(), user1.firstName(), user1.lastName(), user1.phoneNumber(), user1.email());
        BoundaryUserDTO boundaryUser2 = new BoundaryUserDTO(user2.id(), user2.firstName(), user2.lastName(), user2.phoneNumber(), user2.email());

        List<BoundaryUserDTO> boundaryUsers = List.of(boundaryUser1, boundaryUser2);

        // Mock behavior
        when(userGateway.retrieve()).thenReturn(users);
        when(userDTOD2BConverter.process(users)).thenReturn(boundaryUsers);

        // Execute the use case
        List<BoundaryUserDTO> result = interactor.execute();

        // Assertions
        assertThat(result).isEqualTo(boundaryUsers);

        // Verify interactions with mocks
        verify(userGateway).retrieve();
        verify(userDTOD2BConverter).process(users);
    }

    @Test
    void executeShouldReturnEmptyListWhenNoUsersFound() {
        // Prepare test data for an empty list of users
        List<UserDTO> users = List.of();

        // Mock behavior
        when(userGateway.retrieve()).thenReturn(users);

        // Execute the use case
        List<BoundaryUserDTO> result = interactor.execute();

        // Assertions
        assertThat(result).isEmpty();

        // Verify interactions with mocks
        verify(userGateway).retrieve();
    }

    @Test
    void executeShouldReturnConvertedUsers() {
        // Prepare test data for UserDTO (domain model)
        UserDTO user = new UserDTO(UUID.randomUUID(), "John", "Doe", Optional.of("123-456-7890"), Optional.of("john.doe@example.com"));

        // Prepare expected BoundaryUserDTO (boundary model)
        BoundaryUserDTO boundaryUser = new BoundaryUserDTO(user.id(), user.firstName(), user.lastName(), user.phoneNumber(), user.email());

        List<UserDTO> users = List.of(user);
        List<BoundaryUserDTO> boundaryUsers = List.of(boundaryUser);

        // Mock behavior
        when(userGateway.retrieve()).thenReturn(users);
        when(userDTOD2BConverter.process(users)).thenReturn(boundaryUsers);

        // Execute the use case
        List<BoundaryUserDTO> result = interactor.execute();

        // Assertions - check if the user is converted correctly
        assertThat(result.get(0).id()).isEqualTo(boundaryUser.id());
        assertThat(result.get(0).firstName()).isEqualTo(boundaryUser.firstName());
        assertThat(result.get(0).lastName()).isEqualTo(boundaryUser.lastName());
        assertThat(result.get(0).phoneNumber()).isEqualTo(boundaryUser.phoneNumber());
        assertThat(result.get(0).email()).isEqualTo(boundaryUser.email());

        // Verify interactions with mocks
        verify(userGateway).retrieve();
        verify(userDTOD2BConverter).process(users);
    }
}
