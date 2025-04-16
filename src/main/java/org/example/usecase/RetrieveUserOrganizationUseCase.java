package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrganization;

import java.util.UUID;

public interface RetrieveUserOrganizationUseCase {
    Single<BoundaryOrganization> execute(UUID userId);
}
