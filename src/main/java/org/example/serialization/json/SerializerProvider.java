package org.example.serialization.json;

import com.fasterxml.jackson.databind.Module;

import java.util.Objects;

public class SerializerProvider {
    private static JsonSerializer serializer;

    private SerializerProvider() {
    }

    public static JsonSerializer getSerializer() {
        if (Objects.isNull(serializer)) {
            serializer = new JacksonJsonSerializer(new Module[0]);
        }

        return serializer;
    }

    public static JsonSerializer getSerializer(Module... modules) {
        return new JacksonJsonSerializer(modules);
    }
}