package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.UserDTOB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.UserUseCaseFactory;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;
import org.example.serialization.json.JsonSerializer;

import java.util.List;

public class RetrieveUsersRoute extends AuthedRoute<List<BoundaryUserDTO>, List<RestUserDTO>> {
    private final UserUseCaseFactory userUCFactory;
    private final UserDTOB2RConverter userDTOB2RConverter;

    public RetrieveUsersRoute(AuthenticationUseCaseFactory authUCFactory,
                              UserUseCaseFactory userUCFactory,
                              JsonSerializer jsonSerializer,
                              UserDTOB2RConverter userDTOB2RConverter,
                              JavalinExceptionHandler exceptionHandler) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.userUCFactory = userUCFactory;
        this.userDTOB2RConverter = userDTOB2RConverter;
    }

    @Override
    protected List<RestUserDTO> convert(List<BoundaryUserDTO> input) {
        return userDTOB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryUserDTO>> processAuthedRequest(RequestWrapper request) {
        return userUCFactory.createRetrieveUsersUseCase().execute();
    }
}
