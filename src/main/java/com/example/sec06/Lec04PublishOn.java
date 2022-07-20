package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec04PublishOn {
    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
                                    printThreadName("create");
                                    for (int i = 0; i < 5; i++) {
                                        fluxSink.next(i);
                                    }
                                    fluxSink.complete();
                                })
                                .doOnNext(i -> printThreadName("main next " + i));

        flux.publishOn(Schedulers.boundedElastic()) // downstream
            .doOnNext(i -> printThreadName("bound next " + i))
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * create		: Thread: main
         * main next 0		: Thread: main
         * main next 1		: Thread: main
         * main next 2		: Thread: main
         * main next 3		: Thread: main
         * main next 4		: Thread: main
         * bound next 0		: Thread: boundedElastic-1
         * sub 0		: Thread: boundedElastic-1 <- notice this is boundedElastic thread
         * bound next 1		: Thread: boundedElastic-1
         * sub 1		: Thread: boundedElastic-1
         * bound next 2		: Thread: boundedElastic-1
         * sub 2		: Thread: boundedElastic-1
         * bound next 3		: Thread: boundedElastic-1
         * sub 3		: Thread: boundedElastic-1
         * bound next 4		: Thread: boundedElastic-1
         * sub 4		: Thread: boundedElastic-1
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Object> flux2 = Flux.create(fluxSink -> {
                                     printThreadName("create");
                                     for (int i = 0; i < 5; i++) {
                                         fluxSink.next(i);
                                     }
                                     fluxSink.complete();
                                 })
                                 .doOnNext(i -> printThreadName("main next " + i));

        flux2.publishOn(Schedulers.boundedElastic()) // downstream
             .doOnNext(i -> printThreadName("bound next " + i))
             .publishOn(Schedulers.parallel())
             .doOnNext(i -> printThreadName("parallel next " + i))
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * create		: Thread: main
         * main next 0		: Thread: main
         * main next 1		: Thread: main
         * main next 2		: Thread: main
         * main next 3		: Thread: main
         * bound next 0		: Thread: boundedElastic-1
         * main next 4		: Thread: main
         * bound next 1		: Thread: boundedElastic-1
         * bound next 2		: Thread: boundedElastic-1
         * bound next 3		: Thread: boundedElastic-1
         * parallel next 0		: Thread: parallel-1
         * bound next 4		: Thread: boundedElastic-1
         * sub 0		: Thread: parallel-1 <- notice this is parallel thread
         * parallel next 1		: Thread: parallel-1
         * sub 1		: Thread: parallel-1
         * parallel next 2		: Thread: parallel-1
         * sub 2		: Thread: parallel-1
         * parallel next 3		: Thread: parallel-1
         * sub 3		: Thread: parallel-1
         * parallel next 4		: Thread: parallel-1
         * sub 4		: Thread: parallel-1
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
