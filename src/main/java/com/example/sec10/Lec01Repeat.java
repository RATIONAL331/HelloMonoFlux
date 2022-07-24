package com.example.sec10;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * repeat: resubscribe after complete signal
 * retry: resubscribe after error signal
 */
public class Lec01Repeat {
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        getIntegerFlux().repeat(2)
                        .subscribe(new DefaultSubscriber("subscriber"));
        /**
         * subscribe
         * subscriber; Received: 1
         * subscriber; Received: 2
         * subscriber; Received: 3
         * complete <- subscriber not complete
         * subscribe
         * subscriber; Received: 1
         * subscriber; Received: 2
         * subscriber; Received: 3
         * complete
         * subscribe
         * subscriber; Received: 1
         * subscriber; Received: 2
         * subscriber; Received: 3
         * complete
         * subscriber; Completed <- finally completed!
         */

        System.out.println("===============================");

        getIntegerFlux2().repeat(2)
                         .subscribe(new DefaultSubscriber("subscriber2"));

        /**
         * subscribe2
         * subscriber2; Received: 0
         * subscriber2; Received: 1
         * subscriber2; Received: 2
         * complete2
         * subscribe2
         * subscriber2; Received: 3
         * subscriber2; Received: 4
         * subscriber2; Received: 5
         * complete2
         * subscribe2
         * subscriber2; Received: 6
         * subscriber2; Received: 7
         * subscriber2; Received: 8
         * complete2
         * subscriber2; Completed
         */

        System.out.println("===============================");

        getIntegerFlux2().repeat(() -> atomicInteger.get() < 13)
                         //.repeat() // <= infinite repeat
                         .subscribe(new DefaultSubscriber("subscriber3"));

        /**
         * subscribe2
         * subscriber3; Received: 9
         * subscriber3; Received: 10
         * subscriber3; Received: 11
         * complete2
         * subscribe2
         * subscriber3; Received: 12
         * subscriber3; Received: 13
         * subscriber3; Received: 14
         * complete2
         * subscriber3; Completed
         */
    }

    private static Flux<Integer> getIntegerFlux() {
        return Flux.range(1, 3)
                   .doOnSubscribe(sub -> System.out.println("subscribe"))
                   .doOnComplete(() -> System.out.println("complete"));
    }

    private static Flux<Integer> getIntegerFlux2() {
        return Flux.range(1, 3)
                   .doOnSubscribe(sub -> System.out.println("subscribe2"))
                   .doOnComplete(() -> System.out.println("complete2"))
                   .map(i -> atomicInteger.getAndIncrement());
    }
}
