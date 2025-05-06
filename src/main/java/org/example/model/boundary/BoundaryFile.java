package org.example.model.boundary;

import java.io.ByteArrayOutputStream;

public class BoundaryFile {
    private final ByteArrayOutputStream content;
    private final String name;

    public BoundaryFile(ByteArrayOutputStream content, String name) {
        this.content = content;
        this.name = name;
    }

    public ByteArrayOutputStream getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}
