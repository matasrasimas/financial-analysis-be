package org.example.exception;

import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavalinExceptionHandler implements ExceptionHandler<Exception> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavalinExceptionHandler.class);

    @Override
    public void handle(@NotNull Exception e, @NotNull Context context) {
        LOGGER.error("Route exception [{}]", context.fullUrl(), e);
        switch (e) {
            case ItemNotFoundException itemNotFoundException ->
                context.status(404).json(new Message(itemNotFoundException.getMessage()));
            case ExpiredJwtException expiredJwtException ->
                context.status(421).json(new Message(expiredJwtException.getMessage()));
            case SerializationException serializationException ->
                context.status(422).json(new Message(serializationException.getMessage()));
            case NullTokenException nullTokenException ->
                context.status(423).json(new Message(nullTokenException.getMessage()));
            default -> context.status(500).json(new Message("internal server error"));
        }
    }

    public void handle(@NotNull Throwable throwable, @NotNull Context context) {
        switch (throwable) {
            case ItemNotFoundException itemNotFoundException -> handle(itemNotFoundException, context);
            case ExpiredJwtException expiredJwtException -> handle(expiredJwtException, context);
            case SerializationException serializationException -> handle(serializationException, context);
            case NullTokenException nullTokenException -> handle(nullTokenException, context);
            default -> handle(new Exception(), context);
        }
    }

    private record Message(String message) {

    }
}
