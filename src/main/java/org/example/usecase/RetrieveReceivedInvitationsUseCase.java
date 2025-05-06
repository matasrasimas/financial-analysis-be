package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryInvitation;

import java.util.List;
import java.util.UUID;

public interface RetrieveReceivedInvitationsUseCase {
    Single<List<BoundaryInvitation>> execute(UUID receiverId);
}
