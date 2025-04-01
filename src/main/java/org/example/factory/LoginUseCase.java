package org.example.factory;

import org.example.model.boundary.BoundaryAccessToken;
import org.example.model.boundary.BoundaryUserLoginDTO;

import java.util.Optional;

public interface LoginUseCase {
    Optional<BoundaryAccessToken> execute(BoundaryUserLoginDTO login);
}
