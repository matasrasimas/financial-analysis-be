package org.example.usecase;

import org.example.converter.UserDTOD2BConverter;
import org.example.exception.ItemNotFoundException;
import org.example.exception.SerializationException;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;

import java.util.Optional;
import java.util.UUID;

public class VerificationInteractor implements VerificationUseCase {
    private final UserGateway userGateway;
    private final UserDTOD2BConverter userDTOD2BConverter;
    private final TokenValidator tokenValidator;

    public VerificationInteractor(UserGateway userGateway,
                                  UserDTOD2BConverter userDTOD2BConverter,
                                  TokenValidator tokenValidator) {
        this.userGateway = userGateway;
        this.userDTOD2BConverter = userDTOD2BConverter;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public BoundaryUserDTO verify(String authToken) {
        Optional<String> optUserId = tokenValidator.getUserIdFromJwt(authToken);
        if (optUserId.isEmpty())
            throw new SerializationException("Failed to serialize jwt");
        String userId = optUserId.get();

        Optional<UserDTO> optUser = userGateway.retrieveById(UUID.fromString(userId));
        if (optUser.isEmpty())
            throw new ItemNotFoundException(String.format("user with id [%s] not found", userId));

        return userDTOD2BConverter.process(optUser.get()).orElseThrow();
    }
}
