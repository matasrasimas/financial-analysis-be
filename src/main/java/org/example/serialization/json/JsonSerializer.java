package org.example.serialization.json;

public interface JsonSerializer {
    <T> T toObject(String var1, Class<T> var2);

    String toJson(Object var1);
}
