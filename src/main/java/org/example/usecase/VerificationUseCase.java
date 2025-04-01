package org.example.usecase;

import org.example.model.boundary.BoundaryUserDTO;

public interface VerificationUseCase {
    BoundaryUserDTO verify(String authToken);
}
