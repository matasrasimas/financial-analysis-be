package org.example.factory.implementation;

import org.example.converter.UserDTOD2BConverter;
import org.example.converter.UserLoginB2DConverter;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.LoginUseCase;
import org.example.gateway.OrganizationGateway;
import org.example.gateway.UserGateway;
import org.example.usecase.TokenGenerator;
import org.example.usecase.TokenValidator;
import org.example.usecase.VerificationInteractor;
import org.example.usecase.VerificationUseCase;

public class AuthenticationUseCaseFactoryImpl implements AuthenticationUseCaseFactory {
    private final UserGateway userGateway;
    private final UserLoginB2DConverter userLoginB2DConverter;
    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;
    private final UserDTOD2BConverter userDTOD2BConverter;
    private final OrganizationGateway organizationGateway;

    public AuthenticationUseCaseFactoryImpl(UserGateway userGateway,
                                            UserLoginB2DConverter userLoginB2DConverter,
                                            TokenGenerator tokenGenerator,
                                            TokenValidator tokenValidator,
                                            UserDTOD2BConverter userDTOD2BConverter,
                                            OrganizationGateway organizationGateway) {
        this.userGateway = userGateway;
        this.userLoginB2DConverter = userLoginB2DConverter;
        this.tokenGenerator = tokenGenerator;
        this.tokenValidator = tokenValidator;
        this.userDTOD2BConverter = userDTOD2BConverter;
        this.organizationGateway = organizationGateway;
    }

    @Override
    public VerificationUseCase buildVerificationUseCase() {
        return new VerificationInteractor(userGateway, userDTOD2BConverter, tokenValidator);
    }

    @Override
    public LoginUseCase buildLoginUseCase() {
        return new LoginInteractor(userGateway, userLoginB2DConverter, tokenGenerator, organizationGateway);
    }
}
