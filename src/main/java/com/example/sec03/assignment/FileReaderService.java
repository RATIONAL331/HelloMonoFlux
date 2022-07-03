package com.example.sec03.assignment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FileReaderService {
    private Callable<BufferedReader> openReader(Path path) {
        return () -> Files.newBufferedReader(path);
    }

    private BiFunction<BufferedReader, SynchronousSink<String>, BufferedReader> readLine() {
        return (reader, sink) -> {
            try {
                System.out.println("reading");
                String line = reader.readLine();
                if (line != null) {
                    sink.next(line);
                } else {
                    sink.complete();
                }
            } catch (IOException e) {
                sink.error(e);
            }
            return reader;
        };
    }

    private Consumer<BufferedReader> closeReader() {
        return reader -> {
            try {
                System.out.println("close");
                reader.close();
            } catch (IOException e) {
                // ignore
            }
        };
    }

    public Flux<String> read(Path path) {
        return Flux.generate(openReader(path), readLine(), closeReader());
    }
}
