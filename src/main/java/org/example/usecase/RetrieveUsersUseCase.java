package org.example.usecase;

import org.example.model.boundary.BoundaryUserDTO;

import java.util.List;

public interface RetrieveUsersUseCase {
    List<BoundaryUserDTO> execute();
}
