package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import org.example.converter.UserDTOB2DConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.usecase.UpdateUserUseCase;

public class UpdateUserInteractor implements UpdateUserUseCase {
    private final UserGateway userGateway;
    private final UserDTOB2DConverter userDTOB2DConverter;

    public UpdateUserInteractor(UserGateway userGateway,
                                UserDTOB2DConverter userDTOB2DConverter) {
        this.userGateway = userGateway;
        this.userDTOB2DConverter = userDTOB2DConverter;
    }

    @Override
    public Completable execute(BoundaryUserDTO input) {
        return Completable.fromAction(() -> userGateway.update(userDTOB2DConverter.process(input).orElseThrow()));
    }
}
