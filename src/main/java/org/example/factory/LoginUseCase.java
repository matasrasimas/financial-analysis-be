package org.example.factory;

import org.example.model.boundary.BoundaryLoginMetadata;
import org.example.model.boundary.BoundaryUserLoginDTO;

import java.util.Optional;

public interface LoginUseCase {
    Optional<BoundaryLoginMetadata> execute(BoundaryUserLoginDTO login);
}
