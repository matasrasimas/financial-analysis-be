package org.example.factory;

import org.example.usecase.VerificationUseCase;

public interface AuthenticationUseCaseFactory {
    VerificationUseCase buildVerificationUseCase();
    LoginUseCase buildLoginUseCase();
}
