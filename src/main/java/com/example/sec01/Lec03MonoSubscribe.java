package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;

public class Lec03MonoSubscribe {
    public static void main(String[] args) {
        Mono<String> ball = Mono.just("ball");
        // 1. just emit item
        // ball.subscribe();

        // 2. subscribe
        ball.subscribe(item -> System.out.println(item),
                       err -> System.out.println(err.getMessage()), // no error
                       () -> System.out.println("completed"));

        System.out.println("==============================");

        Mono<Integer> ball2 = Mono.just("ball2")
                                  .map(String::length)
                                  .map(len -> len / 0);

        // 3. on error
        ball2.subscribe(item -> System.out.println(item),
                        err -> System.out.println(err.getMessage()), // error
                        () -> System.out.println("completed"));

        System.out.println("==============================");
        Mono<Integer> ball3 = Mono.just("ball3")
                                  .map(String::length)
                                  .map(len -> len / 0);


        ball3.subscribe(Util.onNext(),
                        Util.onError(), // error
                        Util.onComplete());
    }
}
