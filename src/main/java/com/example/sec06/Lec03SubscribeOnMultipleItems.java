package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

// Schedulers != Parallel Execution
// All operation always executed in sequential
// Data is processed one by one via 1 thread in the ThreadPool for a subscriber
public class Lec03SubscribeOnMultipleItems {
    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
                                    printThreadName("create");
                                    for (int i = 0; i < 5; i++) {
                                        fluxSink.next(i);
                                        Util.sleepSeconds(1);
                                    }
                                    fluxSink.complete();
                                })
                                .doOnNext(i -> printThreadName("doOnNext " + i));

        flux.subscribeOn(Schedulers.boundedElastic())
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * create		: Thread: boundedElastic-1 <- same thread
         * doOnNext 0		: Thread: boundedElastic-1 <- same thread
         * sub 0		: Thread: boundedElastic-1 <- same thread
         * doOnNext 1		: Thread: boundedElastic-1 <- ...
         * sub 1		: Thread: boundedElastic-1
         * doOnNext 2		: Thread: boundedElastic-1
         * sub 2		: Thread: boundedElastic-1
         * doOnNext 3		: Thread: boundedElastic-1
         * sub 3		: Thread: boundedElastic-1
         * doOnNext 4		: Thread: boundedElastic-1
         * sub 4		: Thread: boundedElastic-1
         */
        Util.sleepSeconds(5);
        System.out.println("===============================");

        Runnable runnable = () -> flux.subscribeOn(Schedulers.boundedElastic())
                                      .subscribe(v -> printThreadName("sub " + v));
        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }

        /**
         * create		: Thread: boundedElastic-1
         * doOnNext 0		: Thread: boundedElastic-1
         * sub 0		: Thread: boundedElastic-1
         * doOnNext 1		: Thread: boundedElastic-1
         * sub 1		: Thread: boundedElastic-1
         * doOnNext 2		: Thread: boundedElastic-1
         * sub 2		: Thread: boundedElastic-1
         * doOnNext 3		: Thread: boundedElastic-1
         * sub 3		: Thread: boundedElastic-1
         * doOnNext 4		: Thread: boundedElastic-1
         * sub 4		: Thread: boundedElastic-1
         * ===============================
         * create		: Thread: boundedElastic-2
         * doOnNext 0		: Thread: boundedElastic-2
         * create		: Thread: boundedElastic-3
         * doOnNext 0		: Thread: boundedElastic-3
         * sub 0		: Thread: boundedElastic-3
         * create		: Thread: boundedElastic-4
         * sub 0		: Thread: boundedElastic-2
         * doOnNext 0		: Thread: boundedElastic-4
         * sub 0		: Thread: boundedElastic-4
         * doOnNext 1		: Thread: boundedElastic-2
         * sub 1		: Thread: boundedElastic-2
         * doOnNext 1		: Thread: boundedElastic-4
         * sub 1		: Thread: boundedElastic-4
         * doOnNext 1		: Thread: boundedElastic-3
         * sub 1		: Thread: boundedElastic-3
         * doOnNext 2		: Thread: boundedElastic-3
         * sub 2		: Thread: boundedElastic-3
         * doOnNext 2		: Thread: boundedElastic-4
         * sub 2		: Thread: boundedElastic-4
         * doOnNext 2		: Thread: boundedElastic-2
         * sub 2		: Thread: boundedElastic-2
         * doOnNext 3		: Thread: boundedElastic-3
         * sub 3		: Thread: boundedElastic-3
         * doOnNext 3		: Thread: boundedElastic-4
         * doOnNext 3		: Thread: boundedElastic-2
         * sub 3		: Thread: boundedElastic-2
         * sub 3		: Thread: boundedElastic-4
         * doOnNext 4		: Thread: boundedElastic-3
         * doOnNext 4		: Thread: boundedElastic-2
         * doOnNext 4		: Thread: boundedElastic-4
         * sub 4		: Thread: boundedElastic-2
         * sub 4		: Thread: boundedElastic-3
         * sub 4		: Thread: boundedElastic-4
         */

        Util.sleepSeconds(5);
        System.out.println("===============================");

        Flux<Object> flux2 = Flux.create(fluxSink -> {
                                     printThreadName("create");
                                     for (int i = 0; i < 5; i++) {
                                         fluxSink.next(i);
                                         Util.sleepSeconds(1);
                                     }
                                     fluxSink.complete();
                                 })
                                 .doOnNext(i -> printThreadName("doOnNext " + i));

        flux2.subscribeOn(Schedulers.parallel())
             .subscribe(v -> printThreadName("sub " + v));
        /**
         * create		: Thread: parallel-1 <- same thread (Schedulers.parallel() does not mean Parallel Execution)
         * doOnNext 0		: Thread: parallel-1 <- same thread
         * sub 0		: Thread: parallel-1 <- same thread
         * doOnNext 1		: Thread: parallel-1 <- ...
         * sub 1		: Thread: parallel-1
         * doOnNext 2		: Thread: parallel-1
         * sub 2		: Thread: parallel-1
         * doOnNext 3		: Thread: parallel-1
         * sub 3		: Thread: parallel-1
         * doOnNext 4		: Thread: parallel-1
         * sub 4		: Thread: parallel-1
         */

        Util.sleepSeconds(5);
        System.out.println("===========================");

        Flux<Object> flux3 = Flux.create(fluxSink -> {
                                     printThreadName("create");
                                     for (int i = 0; i < 5; i++) {
                                         fluxSink.next(i);
                                         Util.sleepSeconds(1);
                                     }
                                     fluxSink.complete();
                                 })
                                 .doOnNext(i -> printThreadName("doOnNext " + i));

        flux3.subscribeOn(Schedulers.boundedElastic())
             .subscribe(v -> printThreadName("bound sub" + v));

        flux3.subscribeOn(Schedulers.parallel())
             .subscribe(v -> printThreadName("parallel sub " + v));

        /**
         * create		: Thread: boundedElastic-2
         * doOnNext 0		: Thread: boundedElastic-2
         * bound sub0		: Thread: boundedElastic-2
         * create		: Thread: parallel-2 <- notice
         * doOnNext 0		: Thread: parallel-2
         * parallel sub 0		: Thread: parallel-2
         * doOnNext 1		: Thread: boundedElastic-2
         * bound sub1		: Thread: boundedElastic-2
         * doOnNext 1		: Thread: parallel-2
         * parallel sub 1		: Thread: parallel-2
         * doOnNext 2		: Thread: parallel-2
         * parallel sub 2		: Thread: parallel-2
         * doOnNext 2		: Thread: boundedElastic-2
         * bound sub2		: Thread: boundedElastic-2
         * doOnNext 3		: Thread: parallel-2
         * parallel sub 3		: Thread: parallel-2
         * doOnNext 3		: Thread: boundedElastic-2
         * bound sub3		: Thread: boundedElastic-2
         * doOnNext 4		: Thread: boundedElastic-2
         * bound sub4		: Thread: boundedElastic-2
         * doOnNext 4		: Thread: parallel-2
         * parallel sub 4		: Thread: parallel-2
         */

        Util.sleepSeconds(5);
        System.out.println("===========================");
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
