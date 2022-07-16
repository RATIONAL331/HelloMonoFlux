package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec09SwitchIfEmpty {
    public static void main(String[] args) {
        getOrderNumbers().filter(i -> i > 10) // empty
                         .switchIfEmpty(fallback())
                         .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        getOrderNumbers2().filter(i -> i > 10) // not empty
                          .switchIfEmpty(fallback())
                          .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }

    private static Flux<Integer> getOrderNumbers() {
        return Flux.range(1, 10);
    }

    private static Flux<Integer> getOrderNumbers2() {
        return Flux.range(1, 11);
    }

    private static Flux<Integer> fallback() {
        return Flux.range(20, 5);
    }

}
