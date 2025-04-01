package org.example.factory.implementation;

import org.example.converter.UserLoginB2DConverter;
import org.example.factory.LoginUseCase;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryAccessToken;
import org.example.model.boundary.BoundaryUserLoginDTO;
import org.example.usecase.TokenGenerator;

import java.util.Optional;

public class LoginInteractor implements LoginUseCase {
    private final UserGateway userGateway;
    private final UserLoginB2DConverter converter;
    private final TokenGenerator tokenGenerator;

    public LoginInteractor(UserGateway userGateway,
                           UserLoginB2DConverter converter,
                           TokenGenerator tokenGenerator) {
        this.userGateway = userGateway;
        this.converter = converter;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Optional<BoundaryAccessToken> execute(BoundaryUserLoginDTO login) {
        Optional<String> loggedUserId = userGateway.login(converter.process(login).orElseThrow());
        return loggedUserId.map(id -> new BoundaryAccessToken(tokenGenerator.generate(id)));
    }
}
