package org.example.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.serialization.json.exception.JsonSerializingException;

import java.io.IOException;

public class JacksonJsonSerializer implements JsonSerializer {
    private final ObjectMapper mapper;

    JacksonJsonSerializer(Module... modules) {
        this.mapper = this.setUpMapper(modules);
    }

    public String toJson(Object originalObject) {
        try {
            return this.mapper.writeValueAsString(originalObject);
        } catch (JsonProcessingException var3) {
            JsonProcessingException e = var3;
            throw new JsonSerializingException(e);
        }
    }

    public <T> T toObject(String body, Class<T> clazz) {
        try {
            return this.mapper.readValue(body, clazz);
        } catch (IOException var4) {
            IOException e = var4;
            throw new JsonSerializingException(e);
        }
    }

    private ObjectMapper setUpMapper(Module... modules) {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.registerModules(modules);
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(Include.NON_NULL);
        return om;
    }
}
