package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;

import java.util.UUID;

public interface DeleteInvitationUseCase {
    Completable execute(UUID invitationId);
}
