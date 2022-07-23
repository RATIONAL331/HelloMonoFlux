package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec03Latest {
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
            .onBackpressureLatest()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);
        /**
         * emit 0
         * ,,,
         * emit 14
         * subscribe; Received: 0
         * emit 15
         * ...
         * emit 22
         * subscribe; Received: 1
         * emit 23
         * ...
         * emit 30
         * subscribe; Received: 2
         * emit 31
         * ...
         * emit 40
         * subscribe; Received: 3
         * emit 41
         * ...
         * emit 110 <- notice before emit
         * subscribe; Received: 11 <- 75% buffer (16) * zero based index
         * emit 111
         * emit 112
         * ...
         * emit 146
         * emit 147
         * subscribe; Received: 15
         * emit 148
         * ...
         * emit 158
         * subscribe; Received: 110  <- notice Received: 11 before emit
         * emit 159
         * ...
         * emit 498
         * emit 499
         * subscribe; Received: 437
         * subscribe; Received: 438
         * subscribe; Received: 439
         * subscribe; Received: 440
         * subscribe; Received: 441
         * subscribe; Received: 442
         * subscribe; Received: 443
         * subscribe; Received: 444
         * subscribe; Received: 445
         * subscribe; Received: 499
         * subscribe; Completed
         */
    }
}
