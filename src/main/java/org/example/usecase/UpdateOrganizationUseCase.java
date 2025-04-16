package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;
import org.example.model.boundary.BoundaryOrganization;

public interface UpdateOrganizationUseCase {
    Completable execute(BoundaryOrganization input);
}
