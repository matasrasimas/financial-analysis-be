package org.example.route;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.common.RestConstants;
import org.example.converter.EnumMapper;
import org.example.exception.InvalidParamException;
import org.example.exception.NullTokenException;
import org.example.exception.SerializationException;
import org.example.model.rest.RestAccessToken;
import org.example.model.rest.RestUserDTO;
import org.example.serialization.json.JsonSerializer;
import org.example.serialization.json.exception.JsonSerializingException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

public class RequestWrapper {
    public static final int MAX_QUERY_PERIOD_DAYS = 367;
    private final JsonSerializer jsonSerializer;
    private final RestUserDTO authenticatedUser;
    private final Context context;
    private final EnumMapper enumMapper;

    private RequestWrapper(Context context, JsonSerializer jsonSerializer, RestUserDTO authenticatedUser, EnumMapper enumMapper) {
        this.context = context;
        this.jsonSerializer = jsonSerializer;
        this.authenticatedUser = authenticatedUser;
        this.enumMapper = enumMapper;
    }

    public Instant parseInstantParam(String paramKey) {
        return parseParam(paramKey, Instant::parse);
    }

    public LocalDate parseDateParam(String paramKey) {
        return parseParam(paramKey, LocalDate::parse);
    }

    public UploadedFile parseFile() {
        return context.uploadedFile("imageFile");
    }

    public String parseStringParam(String paramKey) {
        return Optional.ofNullable(context.queryParam(paramKey))
                .orElseThrow(() -> new InvalidParamException("Invalid param exception"));
    }

    protected Optional<Integer> getOptionalIntegerParam(String key) {
        try {
            return Optional.of(getIntParam(key));
        } catch (InvalidParamException e) {
            return Optional.empty();
        }
    }

    public Optional<String> getFilterId() {
        return Optional.ofNullable(context.queryParam("filter-id"));
    }

    public int getIntParam(String key) {
        return parseParam(key, Integer::parseInt);
    }

    public Optional<Long> parseOptionalLongParam(String key) {
        if (isNull(context.queryParam(key)))
            return Optional.empty();
        return Optional.of(parseParam(key, Long::parseLong));
    }

    public Optional<String> parseOptionalStringParam(String paramKey) {
        return Optional.ofNullable(context.queryParam(paramKey));
    }

    public Optional<Boolean> parseOptionalBooleanParam(String paramKey) {
        return parseOptionalStringParam(paramKey).map(Boolean::parseBoolean);
    }

    public boolean parseBooleanParam(String paramKey) {
        return parseOptionalBooleanParam(paramKey)
                .orElseThrow(() -> new InvalidParamException("Invalid param exception"));
    }

    public Optional<LocalDate> parseOptionalDateParam(String paramKey) {
        Optional<String> value = parseOptionalStringParam(paramKey);
        try {
            return value.map(LocalDate::parse);
        } catch (DateTimeParseException e) {
            throw new InvalidParamException(String.format("Invalid param exception: %s", e));
        }
    }

    public String getStringPathParam(String key) {
        String param = context.pathParamMap().get(key);
        return Optional.ofNullable(param)
                .filter(value -> !value.isBlank())
                .orElseThrow(() -> new InvalidParamException("Invalid param exception"));
    }

    public List<String> parseOptionalStringsParam(String key) {
        String value = context.queryParam(key);
        return isNull(value) ? null : asList(value.split(","));
    }

    public List<String> parseStringsParam(String key) {
        String value = context.queryParam(key);
        if (isNull(value))
            throw new InvalidParamException("Invalid param exception");
        return asList(value.split(","));
    }

    public RestAccessToken deserializeBearerToken() {
        String bearerToken = context.header(RestConstants.AUTH_HEADER);
        if (isNull(bearerToken))
            throw new NullTokenException("Bearer token cannot be null");
        if (bearerToken.startsWith(RestConstants.BEARER_PREFIX))
            return new RestAccessToken(bearerToken.substring(RestConstants.BEARER_PREFIX.length()));
        throw new SerializationException("Could not deserialize token");
    }

    private <T> T parseParam(String paramKey, Function<String, T> mapper) {
        String paramValue = context.queryParam(paramKey);
        try {
            return mapper.apply(paramValue);
        } catch (DateTimeParseException | NullPointerException | NumberFormatException e) {
            throw new InvalidParamException(String.format("Invalid param exception: %s", e));
        }
    }

    public <T> T deserializeBody(Class<T> type) {
        try {
            T serializedBody = jsonSerializer.toObject(context.body(), type);
            if (isNull(serializedBody))
                throw new SerializationException("Serialization exception");
            return serializedBody;
        } catch (JsonSerializingException e) {
            throw new SerializationException(String.format("Serialization exception: %s", e));
        }
    }


    public <T extends Enum<?>> Optional<T> getEnumParam(String param, T[] values) {
        return parseOptionalStringParam(param)
                .flatMap(value -> enumMapper.map(value, values));
    }

    public String getCookie(String name) {
        return context.cookie(name);
    }

    public RestUserDTO getAuthenticatedUser() {
        return authenticatedUser;
    }

    public String getRequestorId() {
        return authenticatedUser.id();
    }

    public String getBody() {
        return context.body();
    }

    public String getUserAgent() {
        return context.userAgent();
    }

    public List<UploadedFile> getUploadedFiles() {
        return context.uploadedFiles();
    }

    public Optional<String> getHeader(String name) {
        return context.headerMap()
                .entrySet()
                .stream()
                .filter(h -> h.getKey().equalsIgnoreCase(name))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    public <T extends Enum<?>> List<T> getEnumParams(String paramName, T[] values) {
        return parseStringsParam(paramName)
                .stream()
                .map(val -> enumMapper.map(val, values))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public static class Builder {
        private final Context context;
        private final JsonSerializer jsonSerializer;
        private final RestUserDTO authenticateduser;
        private final EnumMapper enumMapper;

        private Builder(Context context,
                        JsonSerializer jsonSerializer,
                        RestUserDTO authenticatedUser,
                        EnumMapper enumMapper) {
            this.context = context;
            this.jsonSerializer = jsonSerializer;
            this.authenticateduser = authenticatedUser;
            this.enumMapper = enumMapper;
        }

        public static RequestWrapper.Builder init(Context context, JsonSerializer jsonSerializer, EnumMapper enumMapper) {
            return new Builder(context, jsonSerializer, null, enumMapper);
        }

        public RequestWrapper.Builder withAuthenticatedUser(RestUserDTO authenticatedUser) {
            return new Builder(context, jsonSerializer, authenticatedUser, enumMapper);
        }

        public static RequestWrapper.Builder of(RequestWrapper requestWrapper) {
            return new RequestWrapper.Builder(requestWrapper.context, requestWrapper.jsonSerializer, requestWrapper.authenticatedUser, requestWrapper.enumMapper);
        }

        public RequestWrapper build() {
            return new RequestWrapper(context, jsonSerializer, authenticateduser, enumMapper);
        }
    }
}
