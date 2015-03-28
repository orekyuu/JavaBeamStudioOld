package net.orekyuu.javatter.api.column;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public enum ColumnType {
    FILE {
        @Override
        InputStream createInputStream(String path) throws IOException {
            return Files.newInputStream(Paths.get(path));
        }
    }, JAR {
        @Override
        InputStream createInputStream(String path) throws IOException {
            return getClass().getResourceAsStream(path);
        }
    }, URL {
        @Override
        InputStream createInputStream(String path) throws IOException {
            return new URL(path).openStream();
        }
    };

    abstract InputStream createInputStream(String path) throws IOException;
}
