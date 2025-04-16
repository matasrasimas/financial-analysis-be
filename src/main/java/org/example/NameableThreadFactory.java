package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameableThreadFactory implements ThreadFactory {
    private final AtomicInteger threadsNum = new AtomicInteger();
    private final String namePattern;
    private final Boolean daemon;

    public NameableThreadFactory(String baseName) {
        this(baseName, null);
    }

    public NameableThreadFactory(String baseName, Boolean daemon) {
        namePattern = baseName + "-%d";
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Thread thread = new Thread(runnable, String.format(namePattern, threadsNum.getAndAdd(1)));
        if (Objects.nonNull(daemon))
            thread.setDaemon(daemon);
        return thread;
    }
}
