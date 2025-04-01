package org.example.gateway;

import org.example.model.domain.UserCreate;
import org.example.model.domain.UserDTO;
import org.example.model.domain.UserLoginDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    List<UserDTO> retrieve();
    Optional<UserDTO> retrieveById(UUID id);
    void create(UserCreate input);
    void update(UserDTO input);
    void delete(UUID id);
    Optional<String> login(UserLoginDTO login);
}
