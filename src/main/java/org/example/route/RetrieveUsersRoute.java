package org.example.route;

import io.javalin.http.Context;
import io.reactivex.rxjava3.core.Single;
import org.example.converter.UserDTOB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.UserUseCaseFactory;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;
import org.example.serialization.json.JsonSerializer;

import java.util.List;

public class RetrieveUsersRoute {
    private final UserUseCaseFactory userUCFactory;
    private final UserDTOB2RConverter userDTOB2RConverter;

    public RetrieveUsersRoute(UserUseCaseFactory userUCFactory,
                              UserDTOB2RConverter userDTOB2RConverter) {
        this.userUCFactory = userUCFactory;
        this.userDTOB2RConverter = userDTOB2RConverter;
    }

    public void processRequest(Context context) {
        List<BoundaryUserDTO> boundaryUsers = userUCFactory.createRetrieveUsersUseCase().execute();
        List<RestUserDTO> restUsers = userDTOB2RConverter.process(boundaryUsers);
        context.status(200).json(restUsers);
    }
}
