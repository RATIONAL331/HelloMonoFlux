package com.example.sec01;

import com.example.sec01.assignment.FileService;
import com.example.util.Util;
import reactor.core.publisher.Mono;

public class Lec09AssignmentDemo {
    public static void main(String[] args) {
        Mono<String> read1 = FileService.read("file01.txt");
        read1.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Mono<String> read3 = FileService.read("file03.txt");
        read3.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        FileService.write("file03.txt", "Hello World!")
                   .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        FileService.delete("file03.txt")
                   .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
