package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryUserDTO;

import java.util.List;

public interface RetrieveUsersUseCase {
    Single<List<BoundaryUserDTO>> execute();
}
