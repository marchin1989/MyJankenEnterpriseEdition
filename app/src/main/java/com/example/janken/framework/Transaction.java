package com.example.janken.framework;

public interface Transaction extends AutoCloseable {
    void commit();

    @Override
    void close();
}
