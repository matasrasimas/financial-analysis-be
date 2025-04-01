package org.example.usecase;

import org.example.model.boundary.BoundaryUserCreate;

public interface CreateUserUseCase {
    void execute(BoundaryUserCreate input);
}
