package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec02Drop_01 {
    public static void main(String[] args) {

        Flux.create(fluxSink -> {
                for (int i = 0; i < 500; i++) {
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                }
                fluxSink.complete();
            })
            //.onBackpressureBuffer()
            .onBackpressureDrop()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        System.out.println("===============================");
        Util.sleepSeconds(5);

        /**
         * emit 0
         * emit 1
         * emit 2
         * ...
         * emit 497
         * emit 498
         * emit 499
         * subscribe; Received: 0
         * subscribe; Received: 1
         * subscribe; Received: 2
         * ...
         * subscribe; Received: 253
         * subscribe; Received: 254
         * subscribe; Received: 255 <- notice
         * subscribe; Completed
         */
    }
}
