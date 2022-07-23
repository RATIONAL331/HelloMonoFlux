package com.example.sec08;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec06Assignment {

    public static void main(String[] args) {
        final int carPrice = 10000;
        Flux.combineLatest(secondStream(), demandStream(), (sec, demand) -> (10000 - (sec * 100)) * demand)
            .subscribe(new DefaultSubscriber("subscriber"));

        Util.sleepSeconds(10);

        /**
         * subscriber; Received: 10000.0
         * subscriber; Received: 9900.0
         * subscriber; Received: 9800.0
         * subscriber; Received: 9700.0
         * subscriber; Received: 10767.000000000002 <- demand factor affected
         * subscriber; Received: 10656.000000000002
         * subscriber; Received: 10545.000000000002
         * subscriber; Received: 10434.000000000002
         * subscriber; Received: 8648.0
         * subscriber; Received: 8556.0
         * subscriber; Received: 8464.0
         * subscriber; Received: 8372.0
         * subscriber; Received: 8736.0
         * subscriber; Received: 8640.0
         */
    }

    private static Flux<Long> secondStream() {
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(1)); // start right now
    }

    private static Flux<Double> demandStream() {
        return Flux.interval(Duration.ofSeconds(3))
                   .map(i -> Util.faker().random().nextInt(80, 120) / 100.0)
                   .startWith(1.0);
    }
}
