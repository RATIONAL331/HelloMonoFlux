package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec05PublishOnSubscribeOn {
    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
                                    printThreadName("create");
                                    for (int i = 0; i < 5; i++) {
                                        fluxSink.next(i);
                                    }
                                    fluxSink.complete();
                                })
                                .doOnNext(i -> printThreadName("next#1 " + i));

        flux.publishOn(Schedulers.parallel()) // downstream
            .doOnNext(i -> printThreadName("next#2 " + i))
            .subscribeOn(Schedulers.boundedElastic()) // upstream
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * create		: Thread: boundedElastic-1
         * next#1 0		: Thread: boundedElastic-1
         * next#1 1		: Thread: boundedElastic-1
         * next#1 2		: Thread: boundedElastic-1
         * next#1 3		: Thread: boundedElastic-1
         * next#2 0		: Thread: parallel-1
         * next#1 4		: Thread: boundedElastic-1
         * sub 0		: Thread: parallel-1
         * next#2 1		: Thread: parallel-1
         * sub 1		: Thread: parallel-1
         * next#2 2		: Thread: parallel-1
         * sub 2		: Thread: parallel-1
         * next#2 3		: Thread: parallel-1
         * sub 3		: Thread: parallel-1
         * next#2 4		: Thread: parallel-1
         * sub 4		: Thread: parallel-1
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
