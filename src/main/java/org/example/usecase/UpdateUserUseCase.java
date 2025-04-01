package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.model.boundary.BoundaryUserDTO;

public interface UpdateUserUseCase {
    Completable execute(BoundaryUserDTO input);
}
