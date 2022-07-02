package com.example.sec01.assignment;

import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {
    private static final Path FILE_PATH = Paths.get("src/main/resources/assignment/sec01");

    public static Mono<String> read(String fileName) {
        return Mono.fromSupplier(() -> readFile(fileName));
    }

    public static Mono<Void> write(String fileName, String content) {
        return Mono.fromRunnable(() -> writeFile(fileName, content));
    }

    public static Mono<String> delete(String fileName) {
        return Mono.fromRunnable(() -> deleteFile(fileName));
    }

    private static String readFile(String fileName) {
        try {
            return Files.readString(FILE_PATH.resolve(fileName));
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found", e);
        }
    }

    private static void writeFile(String fileName, String content) {
        try {
            Files.writeString(FILE_PATH.resolve(fileName), content);
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found", e);
        }
    }

    private static void deleteFile(String fileName) {
        try {
            Files.delete(FILE_PATH.resolve(fileName));
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found", e);
        }
    }
}
