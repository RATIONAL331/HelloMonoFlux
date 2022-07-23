package com.example.sec08;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05CombineLatest {
    public static void main(String[] args) {
        Flux.combineLatest(getString(), getNumber(), (str, num) -> str + num)
            .subscribe(new DefaultSubscriber("subscriber"));

        Util.sleepSeconds(10);

        /**
         * subscriber; Received: a2
         * subscriber; Received: a3
         * subscriber; Received: b3
         * subscriber; Received: c3
         * subscriber; Received: d3
         * subscriber; Completed
         */
    }

    private static Flux<String> getString() {
        return Flux.just("a", "b", "c", "d")
                   .delayElements(Duration.ofSeconds(2));
    }

    private static Flux<Integer> getNumber() {
        return Flux.just(1, 2, 3)
                   .delayElements(Duration.ofSeconds(1));
    }
}
