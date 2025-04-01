package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryOrganization;

import java.util.List;

public interface RetrieveOrganizationsUseCase {
    Single<List<BoundaryOrganization>> execute();
}
