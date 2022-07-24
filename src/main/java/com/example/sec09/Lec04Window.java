package com.example.sec09;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * window difference return Flux (buffer return List)
 */
public class Lec04Window {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        eventStream().window(5)
                     .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: UnicastProcessor
         * subscriber; Received: UnicastProcessor
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");

        eventStream().window(5)
                     //.window(Duration.ofSeconds(1))
                     .flatMap(Lec04Window::saveEvents)
                     .subscribe(new DefaultSubscriber("subscriber2"));

        /**
         * Received: event0
         * Received: event1
         * Received: event2
         * Received: event3
         * Received: event4
         * Completed===========
         * subscriber2; Received: 0
         * Received: event5
         * Received: event6
         * Received: event7
         * Received: event8
         * Received: event9
         * Completed===========
         * subscriber2; Received: 1
         * Received: event10
         * Received: event11
         * Received: event12
         * Received: event13
         * Received: event14
         * Completed===========
         * subscriber2; Received: 2
         * Received: event15
         * Received: event16
         * Received: event17
         * Received: event18
         * Received: event19
         * Completed===========
         * subscriber2; Received: 3
         * Received: event20
         * Received: event21
         * Received: event22
         */

        Util.sleepSeconds(5);
    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                   .map(i -> "event" + i);
    }

    private static Mono<Integer> saveEvents(Flux<String> flux) {
        return flux.doOnNext(event -> System.out.println("Received: " + event))
                   .doOnComplete(() -> System.out.println("Completed==========="))
                   //.then(); // simply complete signal return Mono<Void>
                   .then(Mono.just(counter.getAndIncrement())); // return Mono<Integer>
    }
}
