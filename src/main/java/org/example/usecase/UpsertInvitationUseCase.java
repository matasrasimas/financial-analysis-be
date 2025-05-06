package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryInvitation;

public interface UpsertInvitationUseCase {
    Single<BoundaryInvitation> execute(BoundaryInvitation input);
}
