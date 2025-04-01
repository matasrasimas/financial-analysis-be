package org.example.factory;

import org.example.usecase.CreateUserUseCase;
import org.example.usecase.DeleteUserUseCase;
import org.example.usecase.RetrieveUsersUseCase;
import org.example.usecase.UpdateUserUseCase;

public interface UserUseCaseFactory {
    RetrieveUsersUseCase createRetrieveUsersUseCase();
    CreateUserUseCase createCreateUserUseCase();
    UpdateUserUseCase createUpdateUserUseCase();
    DeleteUserUseCase createDeleteUserUseCase();
}
