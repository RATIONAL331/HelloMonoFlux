package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec04Error {
    public static void main(String[] args) {
        System.setProperty("reactor.bufferSize.small", "16");
        Flux.create(fluxSink -> {
                for (int i = 0; i < 500; i++) {
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            .onBackpressureError()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        System.out.println("===============================");
        Util.sleepSeconds(3);

        /**
         * emit 133
         * emit 134
         * emit 135
         * emit 136
         * subscribe; Received: 15
         * subscribe; Error: reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals than expected (bounded queue...)
         * emit 137 <- still emit item
         * emit 138
         */

        Flux.create(fluxSink -> {
                for (int i = 0; i < 500 && !fluxSink.isCancelled(); i++) { // <- notice !fluxSink.isCancelled()
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            .onBackpressureError()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);

        /**
         * emit 13
         * emit 14
         * emit 15
         * emit 16
         * subscribe; Received: 1
         * subscribe; Received: 2
         * subscribe; Received: 3
         * subscribe; Received: 4
         * subscribe; Received: 5
         * subscribe; Received: 6
         * subscribe; Received: 7
         * subscribe; Received: 8
         * subscribe; Received: 9
         * subscribe; Received: 10
         * subscribe; Received: 11
         * subscribe; Received: 12
         * subscribe; Received: 13
         * subscribe; Received: 14
         * subscribe; Received: 15
         * subscribe; Error: reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals than expected (bounded queue...)
         */
    }
}
