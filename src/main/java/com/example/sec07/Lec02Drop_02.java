package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec02Drop_02 {
    public static void main(String[] args) {
        /**
         * public final class Queues
         *     public static final int CAPACITY_UNSURE = Integer.MIN_VALUE;
         *     public static final int XS_BUFFER_SIZE = Math.max(8, Integer.parseInt(System.getProperty("reactor.bufferSize.x", "32")));
         *     public static final int SMALL_BUFFER_SIZE = Math.max(16, Integer.parseInt(System.getProperty("reactor.bufferSize.small", "256"))); <- default
         */

        System.setProperty("reactor.bufferSize.small", "16");
        Flux.create(fluxSink -> {
                for (int i = 0; i < 500; i++) {
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            //.onBackpressureBuffer()
            .onBackpressureDrop()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);

        /**
         * emit 0
         * emit 1
         * ...
         * emit 8
         * subscribe; Received: 0
         * emit 9
         * ...
         * emit 16
         * subscribe; Received: 1
         * emit 17
         * ...
         * emit 26
         * subscribe; Received: 2
         * emit 27
         * ...
         * emit 115
         * emit 116
         * subscribe; Received: 11 <- 75% buffer (16) * zero based index
         * emit 117 <- this is next received
         * emit 118
         * ...
         * emit 151
         * emit 152
         * subscribe; Received: 15
         * emit 153
         * ...
         * emit 160
         * subscribe; Received: 117 <- notice Received: 11
         * emit 161
         * emit 162
         * emit 163
         * ...
         */

    }
}
