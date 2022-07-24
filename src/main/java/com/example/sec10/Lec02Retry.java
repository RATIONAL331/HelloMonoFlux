package com.example.sec10;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class Lec02Retry {
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        getIntegerFlux()
                .retry(2)
                .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscribe
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscribe
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscribe
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscriber; Error: java.lang.ArithmeticException: / by zero
         */
    }

    private static Flux<Integer> getIntegerFlux() {
        return Flux.range(1, 3)
                   .doOnSubscribe(sub -> System.out.println("subscribe"))
                   .doOnComplete(() -> System.out.println("complete"))
                   .map(i -> i / (i - 2))
                   .doOnError(err -> System.out.println("ERR: " + err));
    }
}
