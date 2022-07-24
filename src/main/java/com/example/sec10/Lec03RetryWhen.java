package com.example.sec10;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lec03RetryWhen {
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        getIntegerFlux()
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .subscribe(new DefaultSubscriber("subscriber"));

        Util.sleepSeconds(3);
        /**
         * subscribe
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscribe <- retry 1 second
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscribe
         * subscriber; Received: -1
         * ERR: java.lang.ArithmeticException: / by zero
         * subscriber; Error: reactor.core.Exceptions$RetryExhaustedException: Retries exhausted: 2/2
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
