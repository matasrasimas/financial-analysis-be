package org.example.route;

import io.javalin.http.Context;
import org.example.converter.UserCreateR2BConverter;
import org.example.factory.UserUseCaseFactory;
import org.example.model.boundary.BoundaryUserCreate;
import org.example.model.rest.RestUserCreate;

public class CreateUserRoute {
    private final UserUseCaseFactory userUCFactory;
    private final UserCreateR2BConverter userCreateR2BConverter;

    public CreateUserRoute(UserUseCaseFactory userUCFactory,
                           UserCreateR2BConverter userCreateR2BConverter) {
        this.userUCFactory = userUCFactory;
        this.userCreateR2BConverter = userCreateR2BConverter;
    }

    public void processRequest(Context context) {
        RestUserCreate reqBody = context.bodyAsClass(RestUserCreate.class);
        BoundaryUserCreate boundaryUser = userCreateR2BConverter.process(reqBody).orElseThrow();

        userUCFactory.createCreateUserUseCase().execute(boundaryUser);

        context.status(204).json("user created successfully");
    }
}
