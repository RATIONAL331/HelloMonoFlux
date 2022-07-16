package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec07Timeout {
    public static void main(String[] args) {
        getOrderNumbers().timeout(Duration.ofMillis(500), fallback())
                         .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Util.sleepSeconds(3);
    }

    private static Flux<Integer> getOrderNumbers() {
        return Flux.range(1, 10)
                   .delayElements(Duration.ofSeconds(1));
    }

    private static Flux<Integer> fallback() {
        return Flux.range(100, 10)
                   .delayElements(Duration.ofMillis(100));
    }
}
