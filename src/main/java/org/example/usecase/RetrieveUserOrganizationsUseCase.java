package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrganization;

import java.util.List;
import java.util.UUID;

public interface RetrieveUserOrganizationsUseCase {
    Single<List<BoundaryOrganization>> execute(UUID userId);
}
