package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.UserDTOR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.UserUseCaseFactory;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;
import org.example.serialization.json.JsonSerializer;

public class UpdateUserRoute extends WithoutResponseBodyRoute {
    private final UserUseCaseFactory userUCFactory;
    private final UserDTOR2BConverter userDTOR2BConverter;

    public UpdateUserRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                           JsonSerializer jsonSerializer,
                           JavalinExceptionHandler exceptionHandler,
                           UserUseCaseFactory userUCFactory,
                           UserDTOR2BConverter userDTOR2BConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.userUCFactory = userUCFactory;
        this.userDTOR2BConverter = userDTOR2BConverter;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        RestUserDTO reqBody = request.deserializeBody(RestUserDTO.class);
        BoundaryUserDTO boundaryUser = userDTOR2BConverter.process(reqBody).orElseThrow();

        return userUCFactory.createUpdateUserUseCase().execute(boundaryUser);
    }
}