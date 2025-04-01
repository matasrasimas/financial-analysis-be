package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.Converter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;
import org.example.serialization.json.JsonSerializer;

public abstract class AuthedRoute<I, O> extends BaseRoute<I, O> {
    private final AuthenticationUseCaseFactory authUCFactory;

    public AuthedRoute(AuthenticationUseCaseFactory authUCFactory,
                       JsonSerializer jsonSerializer,
                       Converter<I, O> converter,
                       JavalinExceptionHandler exceptionHandler) {
        super(jsonSerializer, converter, exceptionHandler);
        this.authUCFactory = authUCFactory;
    }

    @Override
    public Single<I> process(RequestWrapper request) {
        RestUserDTO authenticateduser = authenticate(request);
        return processAuthedRequest(addUserToRequestWrapper(request, authenticateduser));
    }

    protected abstract Single<I> processAuthedRequest(RequestWrapper request);

    private RestUserDTO authenticate(RequestWrapper request) {
        String bearerToken = request.deserializeBearerToken().getToken();
        BoundaryUserDTO boundaryUser = authUCFactory.buildVerificationUseCase().verify(bearerToken);
        return createRestUser(boundaryUser);
    }

    private RequestWrapper addUserToRequestWrapper(RequestWrapper request, RestUserDTO authenticatedUser) {
        return RequestWrapper.Builder.of(request)
                .withAuthenticatedUser(authenticatedUser)
                .build();
    }

    private RestUserDTO createRestUser(BoundaryUserDTO boundaryUser) {
        return new RestUserDTO(
                boundaryUser.id().toString(),
                boundaryUser.firstName(),
                boundaryUser.lastName(),
                boundaryUser.phoneNumber().orElse(null),
                boundaryUser.email().orElse(null)
        );
    }
}
