package org.example.usecase;

import io.reactivex.rxjava3.core.Completable;

import java.util.UUID;

public interface DeleteOrgUnitUseCase {
    Completable execute(UUID id);
}
