package org.example.usecase.implementation;

import org.example.converter.UserCreateB2DConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserCreate;
import org.example.usecase.CreateUserUseCase;

public class CreateUserInteractor implements CreateUserUseCase {
    private final UserGateway userGateway;
    private final UserCreateB2DConverter userCreateB2DConverter;

    public CreateUserInteractor(UserGateway userGateway,
                                UserCreateB2DConverter userCreateB2DConverter) {
        this.userGateway = userGateway;
        this.userCreateB2DConverter = userCreateB2DConverter;
    }

    public void execute(BoundaryUserCreate input) {
        userGateway.create(userCreateB2DConverter.process(input).orElseThrow());
    }
}
