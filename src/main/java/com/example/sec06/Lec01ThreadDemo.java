package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec01ThreadDemo {
    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
                                    printThreadName("create");
                                    fluxSink.next(1);
                                })
                                .doOnNext(i -> printThreadName("doOnNext " + i));

        flux.subscribe(v -> printThreadName("sub " + v));

        /**
         * create		: Thread: main
         * doOnNext 1		: Thread: main
         * sub 1		: Thread: main
         */

        Runnable runnable = () -> flux.subscribe(v -> printThreadName("new thread " + v));
        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
        }

        /**
         * create		: Thread: Thread-1
         * create		: Thread: Thread-0
         * doOnNext 1		: Thread: Thread-1
         * doOnNext 1		: Thread: Thread-0
         * new thread 1		: Thread: Thread-1
         * new thread 1		: Thread: Thread-0
         */

        Util.sleepSeconds(1);
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
