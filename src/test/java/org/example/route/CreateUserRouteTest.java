package org.example.route;

import io.javalin.http.Context;
import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationB2RConverter;
import org.example.converter.OrganizationCreateR2BConverter;
import org.example.converter.UserCreateR2BConverter;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.factory.UserUseCaseFactory;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.boundary.BoundaryUserCreate;
import org.example.model.rest.RestOrganizationCreate;
import org.example.model.rest.RestUserCreate;
import org.example.usecase.CreateOrganizationUseCase;
import org.example.usecase.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateUserRouteTest {
    @Mock
    private UserUseCaseFactory userUCF;
    @Mock
    private CreateUserUseCase useCase;
    @Mock
    private UserCreateR2BConverter userCreateR2BConverter;
    @Mock
    private Context context;

    private CreateUserRoute route;

    @BeforeEach
    void setUp() {
        route = new CreateUserRoute(userUCF, userCreateR2BConverter);
    }

    @Test
    void upsertViaUseCase() {
        RestUserCreate restUserCreate = mock(RestUserCreate.class);
        when(context.bodyAsClass(RestUserCreate.class)).thenReturn(restUserCreate);
        BoundaryUserCreate boundaryUserCreate = mock(BoundaryUserCreate.class);
        when(userCreateR2BConverter.process(restUserCreate)).thenReturn(Optional.of(boundaryUserCreate));
        when(userUCF.createCreateUserUseCase()).thenReturn(useCase);
        Context contextWithStatus = mock(Context.class);
        when(context.status(204)).thenReturn(contextWithStatus);

        route.processRequest(context);

        verify(useCase).execute(boundaryUserCreate);
        verify(contextWithStatus).json("user created successfully");
    }
}
