package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.UserDTOD2BConverter;
import org.example.gateway.UserGateway;
import org.example.model.boundary.BoundaryUserDTO;
import org.example.usecase.RetrieveUsersUseCase;

import java.util.List;

public class RetrieveUsersInteractor implements RetrieveUsersUseCase {
    private final UserGateway userGateway;
    private final UserDTOD2BConverter userDTOD2BConverter;

    public RetrieveUsersInteractor(UserGateway userGateway,
                                   UserDTOD2BConverter userDTOD2BConverter) {
        this.userGateway = userGateway;
        this.userDTOD2BConverter = userDTOD2BConverter;
    }

    @Override
    public Single<List<BoundaryUserDTO>> execute() {
        return Single.fromCallable(() -> userDTOD2BConverter.process(userGateway.retrieve()));
    }
}
