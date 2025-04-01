package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.UserUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.USER_ID;

public class DeleteUserRoute extends WithoutResponseBodyRoute {
    private final UserUseCaseFactory userUCFactory;

    public DeleteUserRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                           JsonSerializer jsonSerializer,
                           JavalinExceptionHandler exceptionHandler,
                           UserUseCaseFactory userUCFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.userUCFactory = userUCFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID userId = UUID.fromString(request.getStringPathParam(USER_ID));

        return userUCFactory.createDeleteUserUseCase().execute(userId);
    }
}
