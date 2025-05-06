package org.example.route;

import io.javalin.http.Context;
import jakarta.servlet.ServletOutputStream;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.model.boundary.BoundaryFile;
import org.example.serialization.json.JsonSerializer;

import java.io.IOException;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class XlsxRoute extends AuthedRoute<BoundaryFile, BoundaryFile> {
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_HEADER_KEY = "Content-Disposition";
    private static final String CONTENT_HEADER_VALUE = "attachment; filename=\"%s.xlsx\"";

    public XlsxRoute(AuthenticationUseCaseFactory useCaseFactory,
                     JsonSerializer jsonSerializer,
                     JavalinExceptionHandler exceptionHandler) {
        super(useCaseFactory, jsonSerializer, null, exceptionHandler);
    }

    @Override
    protected void onSuccess(RequestWrapper request, Context ctx, BoundaryFile result) {
        try (ServletOutputStream outputStream = ctx.res().getOutputStream()) {
            ctx.contentType(CONTENT_TYPE);
            ctx.res().setCharacterEncoding(UTF_8.name());
            ctx.header(CONTENT_HEADER_KEY, format(CONTENT_HEADER_VALUE, result.getName()));
            ctx.header("Access-Control-Expose-Headers", "Content-Disposition");
            result.getContent().writeTo(outputStream);
        } catch (IOException e) {
            handleException(ctx, e);
        }
    }
}
