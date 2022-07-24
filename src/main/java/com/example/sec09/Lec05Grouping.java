package com.example.sec09;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05Grouping {
    public static void main(String[] args) {
        Flux.range(1, 30)
            .delayElements(Duration.ofMillis(200))
            .groupBy(b -> b % 2 == 0)
            .subscribe(groupFlux -> process(groupFlux, groupFlux.key()));

        Util.sleepSeconds(5);

        /**
         * process
         * false: 1
         * process <- process two times
         * true: 2
         * false: 3
         * true: 4
         * false: 5
         * true: 6
         * false: 7
         * true: 8
         */
    }

    private static void process(Flux<Integer> flux, boolean key) {
        System.out.println("process");
        flux.subscribe(str -> System.out.println(key + ": " + str));
    }
}
