package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

public class Lec06FluxCreateWithOverflow {
    public static void main(String[] args) {
        System.setProperty("reactor.bufferSize.small", "16");

        Flux.create(fluxSink -> {
                for (int i = 0; i < 500 && !fluxSink.isCancelled(); i++) { // <- notice !fluxSink.isCancelled()
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            }, FluxSink.OverflowStrategy.DROP) // <- create with Overflow strategy
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(7);
    }
}
