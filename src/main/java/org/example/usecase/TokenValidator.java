package org.example.usecase;

import java.util.Optional;

public interface TokenValidator {
    Optional<String> getUserIdFromJwt(String token);
}
