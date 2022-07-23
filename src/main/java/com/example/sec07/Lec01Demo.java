package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec01Demo {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
                for (int i = 0; i < 500; i++) {
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                }
                fluxSink.complete();
            })
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10)) // emit item fast, but doOnNext is slow
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(6);
    }

    /**
     * buffer: keep in memory
     * drop: once the queue is full, new items will be dropped
     * latest: once the queue is full, keep 1 latest item as and when it arrives. drop old
     * error: throw error to the downstream
     */
}
