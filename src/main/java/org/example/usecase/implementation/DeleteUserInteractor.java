package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.ItemNotFoundException;
import org.example.gateway.UserGateway;
import org.example.usecase.DeleteUserUseCase;

import java.util.UUID;

public class DeleteUserInteractor implements DeleteUserUseCase {
    private final UserGateway userGateway;

    public DeleteUserInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public Completable execute(UUID id) {
        return Completable.fromAction(() -> {
            if (userGateway.retrieveById(id).isEmpty())
                throw new ItemNotFoundException(String.format("user with id [%s] is not found", id));
            userGateway.delete(id);
        });
    }
}
