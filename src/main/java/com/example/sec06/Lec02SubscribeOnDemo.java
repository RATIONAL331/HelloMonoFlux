package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Scheduler
 * 1. boundedElastic: Network or Time-Consuming calls
 * 2. parallel: CPU-Intensive task
 * 3. single: A single dedicated thread for one-off task (일회성 작업 전용 스레드)
 * 4. immediate: Current Thread
 */
public class Lec02SubscribeOnDemo {
    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
                                    printThreadName("create");
                                    fluxSink.next(1);
                                })
                                .doOnNext(i -> printThreadName("doOnNext " + i));

        flux.doFirst(() -> printThreadName("doFirst1"))
            .subscribeOn(Schedulers.boundedElastic())
            .doFirst(() -> printThreadName("doFirst2"))
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * doFirst2		: Thread: main <- notice
         * doFirst1		: Thread: boundedElastic-1 <- notice
         * create		: Thread: boundedElastic-1 <- notice same boundElastic-1
         * doOnNext 1		: Thread: boundedElastic-1
         * sub 1		: Thread: boundedElastic-1
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Runnable runnable = () -> flux.doFirst(() -> printThreadName("doFirst1"))
                                      .subscribeOn(Schedulers.boundedElastic())
                                      .doFirst(() -> printThreadName("doFirst2"))
                                      .subscribe(v -> printThreadName("sub " + v));
        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
        }
        /**
         * doFirst2		: Thread: Thread-1
         * doFirst2		: Thread: Thread-0
         * doFirst1		: Thread: boundedElastic-2
         * doFirst1		: Thread: boundedElastic-3
         * create		: Thread: boundedElastic-2
         * create		: Thread: boundedElastic-3
         * doOnNext 1		: Thread: boundedElastic-2
         * doOnNext 1		: Thread: boundedElastic-3
         * sub 1		: Thread: boundedElastic-2
         * sub 1		: Thread: boundedElastic-3
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Object> flux2 = Flux.create(fluxSink -> {
                                     printThreadName("create");
                                     fluxSink.next(1);
                                 })
                                 .subscribeOn(Schedulers.newParallel("newParallel")) // <- notice
                                 .doOnNext(i -> printThreadName("doOnNext " + i));

        flux2.doOnNext(i -> printThreadName("doOnNext#1 " + i))
             .doFirst(() -> printThreadName("doFirst1"))
             .subscribeOn(Schedulers.boundedElastic())
             .doOnNext(i -> printThreadName("doOnNext#2 " + i))
             .doFirst(() -> printThreadName("doFirst2"))
             .doOnNext(i -> printThreadName("doOnNext#3 " + i))
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * doFirst2		: Thread: main
         * doFirst1		: Thread: boundedElastic-4
         * create		: Thread: newParallel-1
         * doOnNext 1		: Thread: newParallel-1
         * doOnNext#1 1		: Thread: newParallel-1
         * doOnNext#2 1		: Thread: newParallel-1
         * doOnNext#3 1		: Thread: newParallel-1
         * sub 1		: Thread: newParallel-1
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Runnable runnable2 = () -> flux2.doFirst(() -> printThreadName("doFirst1"))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .doFirst(() -> printThreadName("doFirst2"))
                                        .subscribe(v -> printThreadName("sub " + v));
        for (int i = 0; i < 2; i++) {
            new Thread(runnable2).start();
        }

        /**
         * doFirst2		: Thread: Thread-2
         * doFirst2		: Thread: Thread-3
         * doFirst1		: Thread: boundedElastic-5
         * doFirst1		: Thread: boundedElastic-6
         * create		: Thread: newParallel-2 <- notice
         * doOnNext 1		: Thread: newParallel-2
         * create		: Thread: newParallel-3
         * sub 1		: Thread: newParallel-2
         * doOnNext 1		: Thread: newParallel-3
         * sub 1		: Thread: newParallel-3
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Object> flux3 = Flux.create(fluxSink -> {
                                     for (int i = 0; i < 5; i++) {
                                         fluxSink.next(i);
                                     }
                                     fluxSink.complete();
                                 })
                                 .subscribeOn(Schedulers.boundedElastic());

        flux3.subscribeOn(Schedulers.parallel())
             .doOnNext(i -> printThreadName("doOnNext#1 " + i))
             .map(i -> i + "a")
             .doOnNext(i -> printThreadName("doOnNext#2 " + i))
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * doOnNext#1 0		: Thread: boundedElastic-7
         * doOnNext#2 0a		: Thread: boundedElastic-7
         * sub 0a		: Thread: boundedElastic-7
         * doOnNext#1 1		: Thread: boundedElastic-7
         * doOnNext#2 1a		: Thread: boundedElastic-7
         * sub 1a		: Thread: boundedElastic-7
         * doOnNext#1 2		: Thread: boundedElastic-7
         * doOnNext#2 2a		: Thread: boundedElastic-7
         * sub 2a		: Thread: boundedElastic-7
         * doOnNext#1 3		: Thread: boundedElastic-7
         * doOnNext#2 3a		: Thread: boundedElastic-7
         * sub 3a		: Thread: boundedElastic-7
         * doOnNext#1 4		: Thread: boundedElastic-7
         * doOnNext#2 4a		: Thread: boundedElastic-7
         * sub 4a		: Thread: boundedElastic-7
         */

        Util.sleepSeconds(1);
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
