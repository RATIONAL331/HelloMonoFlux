package com.example.sec03;

import com.example.sec03.assignment.FileReaderService;
import com.example.util.Util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Lec09FileReaderServiceAssignment {
    public static void main(String[] args) {
        FileReaderService fileReaderService = new FileReaderService();
        Path path = Paths.get("src/main/resources/assignment/sec03").resolve("file01.txt");
        fileReaderService.read(path)
                         //.take(5)
                         .map(i -> {
                             Integer integer = Util.faker().random().nextInt(1, 10);
                             if (integer > 8) {
                                 throw new RuntimeException("error"); // when error file close properly.
                             }
                             return i;
                         })
                         .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
