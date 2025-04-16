package org.example.route;

import io.javalin.http.Context;
import org.example.common.RestConstants;
import org.example.converter.UserDTOB2RConverter;
import org.example.exception.ItemNotFoundException;
import org.example.exception.NullTokenException;
import org.example.exception.SerializationException;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestAccessToken;
import org.example.model.rest.RestUserDTO;

import static java.util.Objects.isNull;

public class AuthenticateUserRoute {
    private final AuthenticationUseCaseFactory authUCFactory;
    private final UserDTOB2RConverter userDTOB2RConverter;

    public AuthenticateUserRoute(AuthenticationUseCaseFactory authUCFactory,
                                 UserDTOB2RConverter userDTOB2RConverter) {
        this.authUCFactory = authUCFactory;
        this.userDTOB2RConverter = userDTOB2RConverter;
    }

    public void processRequest(Context context) {
        try {
            RestAccessToken bearerToken = deserializeBearerToken(context);
            BoundaryUserDTO boundaryUser = authUCFactory.buildVerificationUseCase().verify(bearerToken.getToken());
            RestUserDTO restUser = userDTOB2RConverter.process(boundaryUser).orElseThrow();
            context.status(200).json(restUser);
        } catch (SerializationException | ItemNotFoundException | NullTokenException e) {
            context.status(420).json(new Message(e.getMessage()));
        }

    }

    public RestAccessToken deserializeBearerToken(Context context) {
        String bearerToken = context.header(RestConstants.AUTH_HEADER);
        if (isNull(bearerToken))
            throw new NullTokenException("Bearer token cannot be null");
        if (bearerToken.startsWith(RestConstants.BEARER_PREFIX))
            return new RestAccessToken(bearerToken.substring(RestConstants.BEARER_PREFIX.length()));
        throw new SerializationException("Could not deserialize token");
    }

    private record Message(String message) {

    }
}
