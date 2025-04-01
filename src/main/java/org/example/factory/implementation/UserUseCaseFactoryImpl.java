package org.example.factory.implementation;

import org.example.converter.UserCreateB2DConverter;
import org.example.converter.UserDTOB2DConverter;
import org.example.converter.UserDTOD2BConverter;
import org.example.factory.UserUseCaseFactory;
import org.example.gateway.UserGateway;
import org.example.usecase.CreateUserUseCase;
import org.example.usecase.DeleteUserUseCase;
import org.example.usecase.RetrieveUsersUseCase;
import org.example.usecase.UpdateUserUseCase;
import org.example.usecase.implementation.CreateUserInteractor;
import org.example.usecase.implementation.DeleteUserInteractor;
import org.example.usecase.implementation.RetrieveUsersInteractor;
import org.example.usecase.implementation.UpdateUserInteractor;

public class UserUseCaseFactoryImpl implements UserUseCaseFactory {
    private final UserGateway userGateway;
    private final UserDTOD2BConverter userDTOD2BConverter;
    private final UserDTOB2DConverter userDTOB2DConverter;
    private final UserCreateB2DConverter userCreateB2DConverter;

    public UserUseCaseFactoryImpl(UserGateway userGateway,
                                  UserDTOD2BConverter userDTOD2BConverter,
                                  UserDTOB2DConverter userDTOB2DConverter,
                                  UserCreateB2DConverter userCreateB2DConverter) {
        this.userGateway = userGateway;
        this.userDTOD2BConverter = userDTOD2BConverter;
        this.userDTOB2DConverter = userDTOB2DConverter;
        this.userCreateB2DConverter = userCreateB2DConverter;
    }

    @Override
    public RetrieveUsersUseCase createRetrieveUsersUseCase() {
        return new RetrieveUsersInteractor(userGateway, userDTOD2BConverter);
    }

    @Override
    public CreateUserUseCase createCreateUserUseCase() {
        return new CreateUserInteractor(userGateway, userCreateB2DConverter);
    }

    @Override
    public UpdateUserUseCase createUpdateUserUseCase() {
        return new UpdateUserInteractor(userGateway, userDTOB2DConverter);
    }

    @Override
    public DeleteUserUseCase createDeleteUserUseCase() {
        return new DeleteUserInteractor(userGateway);
    }
}
