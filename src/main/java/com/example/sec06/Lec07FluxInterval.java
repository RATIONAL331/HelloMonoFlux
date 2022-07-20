package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Lec07FluxInterval {
    public static void main(String[] args) {
        Flux.interval(Duration.ofMillis(100)) // <- basic return parallel /* return interval(period, Schedulers.parallel()); */
            .doFirst(() -> printThreadName("doFirst#1"))
            .doOnNext(i -> printThreadName("next#1" + i)) // execution in parallel because Flux#interval execute in parallel
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> printThreadName("next#2" + i))
            .doFirst(() -> printThreadName("doFirst#2"))
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * doFirst#2		: Thread: boundedElastic-1
         * doFirst#1		: Thread: boundedElastic-1
         * next#10		: Thread: parallel-1 <- notice [execution in parallel because Flux#interval execute in parallel]
         * next#20		: Thread: boundedElastic-2
         * sub 0		: Thread: boundedElastic-2
         * next#11		: Thread: parallel-1
         * next#21		: Thread: boundedElastic-2
         * sub 1		: Thread: boundedElastic-2
         * next#12		: Thread: parallel-1
         * next#22		: Thread: boundedElastic-2
         * sub 2		: Thread: boundedElastic-2
         * next#13		: Thread: parallel-1
         * next#23		: Thread: boundedElastic-2
         * sub 3		: Thread: boundedElastic-2
         * next#14		: Thread: parallel-1
         * next#24		: Thread: boundedElastic-2
         * sub 4		: Thread: boundedElastic-2
         * next#15		: Thread: parallel-1
         * next#25		: Thread: boundedElastic-2
         * sub 5		: Thread: boundedElastic-2
         * next#16		: Thread: parallel-1
         * next#26		: Thread: boundedElastic-2
         * sub 6		: Thread: boundedElastic-2
         * next#17		: Thread: parallel-1
         * next#27		: Thread: boundedElastic-2
         * sub 7		: Thread: boundedElastic-2
         * next#18		: Thread: parallel-1
         * next#28		: Thread: boundedElastic-2
         * sub 8		: Thread: boundedElastic-2
         * next#19		: Thread: parallel-1
         * next#29		: Thread: boundedElastic-2
         * sub 9		: Thread: boundedElastic-2
         */

        Util.sleepSeconds(1);
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
