package org.example.route;

import io.javalin.http.Context;
import org.example.converter.UserLoginR2BConverter;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.model.boundary.BoundaryUserLoginDTO;
import org.example.model.rest.RestLoginMetadata;
import org.example.model.rest.RestUserLoginDTO;

public class LoginRoute {
    private final AuthenticationUseCaseFactory authUCFactory;
    private final UserLoginR2BConverter userLoginR2BConverter;

    public LoginRoute(AuthenticationUseCaseFactory authUCFactory,
                      UserLoginR2BConverter userLoginR2BConverter) {
        this.authUCFactory = authUCFactory;
        this.userLoginR2BConverter = userLoginR2BConverter;
    }

    public void processRequest(Context context) {
        RestUserLoginDTO reqBody = context.bodyAsClass(RestUserLoginDTO.class);
        BoundaryUserLoginDTO boundaryUserLoginDTO = userLoginR2BConverter.process(reqBody).orElseThrow();

        authUCFactory.buildLoginUseCase().execute(boundaryUserLoginDTO).ifPresentOrElse(
                loginMetadata -> context.status(200).json(loginMetadata),
                () -> context.status(420).json(new Message("Neteisingas el. paštas arba slaptažodis"))
        );
    }

    private record Message(String message) {

    }
}
