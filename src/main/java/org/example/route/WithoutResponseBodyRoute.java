package org.example.route;

import io.javalin.http.Context;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import static java.lang.Boolean.TRUE;

public abstract class WithoutResponseBodyRoute extends AuthedRoute<Boolean, Boolean> {
    public WithoutResponseBodyRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                    JsonSerializer jsonSerializer,
                                    JavalinExceptionHandler exceptionHandler) {
        super(authenticationUseCaseFactory, jsonSerializer, null, exceptionHandler);
    }

    @Override
    protected Single<Boolean> processAuthedRequest(RequestWrapper request) {
        return processWithoutBody(request).toSingle(() -> true);
    }

    protected abstract Completable processWithoutBody(RequestWrapper request);

    @Override
    protected void fillResponse(Context ctx, int status, Object body) {
        if (TRUE.equals(body))
            super.fillResponse(ctx, 204, null);
        else
            super.fillResponse(ctx, status, body);
    }
}
