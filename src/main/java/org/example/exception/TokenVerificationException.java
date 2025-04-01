package org.example.exception;

public class TokenVerificationException extends RuntimeException {
    public TokenVerificationException(String message) {
        super(message);
    }
}
