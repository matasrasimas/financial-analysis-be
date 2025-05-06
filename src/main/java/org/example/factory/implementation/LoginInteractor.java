package org.example.factory.implementation;

import org.example.converter.UserLoginB2DConverter;
import org.example.factory.LoginUseCase;
import org.example.gateway.OrgUnitGateway;
import org.example.gateway.OrganizationGateway;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryLoginMetadata;
import org.example.model.boundary.BoundaryUserLoginDTO;
import org.example.usecase.TokenGenerator;

import java.util.Optional;
import java.util.UUID;

public class LoginInteractor implements LoginUseCase {
    private final UserGateway userGateway;
    private final UserLoginB2DConverter converter;
    private final TokenGenerator tokenGenerator;
    private final OrganizationGateway organizationGateway;

    public LoginInteractor(UserGateway userGateway,
                           UserLoginB2DConverter converter,
                           TokenGenerator tokenGenerator,
                           OrganizationGateway organizationGateway) {
        this.userGateway = userGateway;
        this.converter = converter;
        this.tokenGenerator = tokenGenerator;
        this.organizationGateway = organizationGateway;
    }

    @Override
    public Optional<BoundaryLoginMetadata> execute(BoundaryUserLoginDTO login) {
        Optional<String> loggedUserId = userGateway.login(converter.process(login).orElseThrow());
        return loggedUserId.map(userId -> {
            Optional<String> activeOrg = organizationGateway.retrieveByUserId(UUID.fromString(userId)).stream().map(org -> org.id().toString()).findFirst();
            return new BoundaryLoginMetadata(tokenGenerator.generate(userId), activeOrg.orElse(null));
        });
    }
}
