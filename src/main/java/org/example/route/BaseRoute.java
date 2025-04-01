package org.example.route;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import org.example.converter.Converter;
import org.example.converter.EnumMapper;
import org.example.exception.ConverterException;
import org.example.exception.JavalinExceptionHandler;
import org.example.exception.NullTokenException;
import org.example.exception.TokenVerificationException;
import org.example.serialization.json.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public abstract class BaseRoute<I, O> implements Handler {
    private final JsonSerializer jsonSerializer;
    private final Converter<I, O> converter;
    private final JavalinExceptionHandler exceptionHandler;


    protected BaseRoute(JsonSerializer jsonSerializer,
                        Converter<I, O> converter,
                        JavalinExceptionHandler exceptionHandler) {
        this.jsonSerializer = jsonSerializer;
        this.converter = converter;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(@NotNull Context ctx) {
        RequestWrapper requestWrapper = RequestWrapper.Builder.init(ctx, jsonSerializer, new EnumMapper()).build();
        Disposable subscription = null;
        try {
            subscription = process(requestWrapper)
                    .map(this::convert)
                    .subscribe(result -> onSuccess(requestWrapper, ctx, result),
                            throwable -> exceptionHandler.handle(throwable, ctx));
        } catch(TokenVerificationException | NullTokenException e) {
            exceptionHandler.handle(e, ctx);
        }
        finally {
            if (nonNull(subscription) && !subscription.isDisposed())
                subscription.dispose();
        }
    }

    protected abstract Single<I> process(RequestWrapper request);

    @SuppressWarnings("unused")
    protected void onSuccess(RequestWrapper request, Context ctx, O result) {
        fillResponse(ctx, 200, result);
    }

    @SuppressWarnings("unchecked")
    protected O convert(I input) {
        if (isNull(converter))
            return (O) input;
        return converter.process(input)
                .orElseThrow(() -> new ConverterException("Failed to execute B2R conversion"));
    }

    protected void fillResponse(Context ctx, int status, Object body) {
        if (nonNull(body))
            ctx.result(jsonSerializer.toJson(body));
        ctx.status(status);
        ctx.contentType("application/json; charset=utf-8");
    }
}
