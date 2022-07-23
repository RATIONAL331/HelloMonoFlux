package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec05BufferWithSize {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
                for (int i = 0; i < 500 && !fluxSink.isCancelled(); i++) { // <- notice !fluxSink.isCancelled()
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            .onBackpressureBuffer(20)
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);
        System.out.println("===============================");


        /**
         * ...
         * emit 14
         * emit 15
         * emit 16
         * subscribe; Received: 0
         * emit 17
         * emit 18
         * ...
         * subscribe; Received: 20
         * subscribe; Received: 21
         * subscribe; Error: reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals than expected (bounded queue...)
         */

        Flux.create(fluxSink -> {
                for (int i = 0; i < 500 && !fluxSink.isCancelled(); i++) { // <- notice !fluxSink.isCancelled()
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            .onBackpressureBuffer(20, o -> System.out.println("drop " + o))
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);
        System.out.println("===============================");

        /**
         * emit 0
         * ...
         * emit 8
         * subscribe; Received: 0
         * emit 9
         * ...
         * emit 17
         * subscribe; Received: 1
         * emit 18
         * ...
         * emit 22
         * drop 23
         * emit 23
         * subscribe; Received: 2
         * subscribe; Received: 3
         * subscribe; Received: 4
         * ...
         * subscribe; Received: 21
         * subscribe; Received: 22
         * subscribe; Error: reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals than expected (bounded queue...)
         */
    }
}
