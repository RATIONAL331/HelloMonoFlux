package com.example.sec06;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class Lec06Parallel {
    public static void main(String[] args) {
        Flux<Integer> flux = Flux.range(1, 10);

        flux.parallel()
            .runOn(Schedulers.parallel())
            .doOnNext(i -> printThreadName("next " + i))
            .subscribe(v -> printThreadName("sub " + v));

        /**
         * next 10		: Thread: parallel-10
         * next 4		: Thread: parallel-4
         * next 1		: Thread: parallel-1
         * sub 10		: Thread: parallel-10
         * next 9		: Thread: parallel-9
         * next 5		: Thread: parallel-5
         * next 2		: Thread: parallel-2
         * next 3		: Thread: parallel-3
         * sub 2		: Thread: parallel-2
         * sub 5		: Thread: parallel-5
         * sub 9		: Thread: parallel-9
         * sub 4		: Thread: parallel-4
         * sub 1		: Thread: parallel-1
         * sub 3		: Thread: parallel-3
         * next 7		: Thread: parallel-7
         * next 6		: Thread: parallel-6
         * sub 7		: Thread: parallel-7
         * sub 6		: Thread: parallel-6
         * next 8		: Thread: parallel-8
         * sub 8		: Thread: parallel-8
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Integer> flux2 = Flux.range(1, 1000);
        List<Object> list = new ArrayList<>(); // <- not thread safe
        flux2.parallel()
             .runOn(Schedulers.parallel())
             .subscribe(list::add);

        System.out.println("list size: " + list.size());
        /**
         * list size: 997
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Integer> flux3 = Flux.range(1, 10);

        flux3.parallel()
             .runOn(Schedulers.boundedElastic())
             .doOnNext(i -> printThreadName("next " + i))
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * next 2		: Thread: boundedElastic-2
         * next 4		: Thread: boundedElastic-4
         * next 1		: Thread: boundedElastic-1
         * next 8		: Thread: boundedElastic-8
         * next 9		: Thread: boundedElastic-9
         * next 7		: Thread: boundedElastic-7
         * next 5		: Thread: boundedElastic-5
         * next 3		: Thread: boundedElastic-3
         * sub 3		: Thread: boundedElastic-3
         * next 6		: Thread: boundedElastic-6
         * sub 5		: Thread: boundedElastic-5
         * sub 7		: Thread: boundedElastic-7
         * sub 9		: Thread: boundedElastic-9
         * sub 8		: Thread: boundedElastic-8
         * sub 1		: Thread: boundedElastic-1
         * sub 2		: Thread: boundedElastic-2
         * sub 4		: Thread: boundedElastic-4
         * next 10		: Thread: boundedElastic-10
         * sub 10		: Thread: boundedElastic-10
         * sub 6		: Thread: boundedElastic-6
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        flux3.parallel(2)
             .runOn(Schedulers.boundedElastic())
             .doOnNext(i -> printThreadName("next " + i))
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * next 1		: Thread: boundedElastic-10
         * next 2		: Thread: boundedElastic-7
         * sub 1		: Thread: boundedElastic-10
         * sub 2		: Thread: boundedElastic-7
         * next 4		: Thread: boundedElastic-7
         * sub 4		: Thread: boundedElastic-7
         * next 3		: Thread: boundedElastic-10
         * next 6		: Thread: boundedElastic-7
         * sub 6		: Thread: boundedElastic-7
         * sub 3		: Thread: boundedElastic-10
         * next 8		: Thread: boundedElastic-7
         * sub 8		: Thread: boundedElastic-7
         * next 10		: Thread: boundedElastic-7
         * next 5		: Thread: boundedElastic-10
         * sub 10		: Thread: boundedElastic-7
         * sub 5		: Thread: boundedElastic-10
         * next 7		: Thread: boundedElastic-10
         * sub 7		: Thread: boundedElastic-10
         * next 9		: Thread: boundedElastic-10
         * sub 9		: Thread: boundedElastic-10
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        Flux<Integer> flux4 = Flux.range(1, 10);
        flux4.parallel()
             .runOn(Schedulers.boundedElastic())
             // doOnNext는 여러개의 스레드에서 수행
             .doOnNext(i -> printThreadName("next " + i))
             .sequential()
             // subscribe는 next가 이루어진 대로 하나의 스레드에서 수행 (바뀔 수는 있음)
             .subscribe(v -> printThreadName("sub " + v));

        /**
         * next 1		: Thread: boundedElastic-3
         * next 10		: Thread: boundedElastic-12
         * next 8		: Thread: boundedElastic-6
         * sub 1		: Thread: boundedElastic-3
         * sub 8		: Thread: boundedElastic-3
         * sub 10		: Thread: boundedElastic-3
         * next 7		: Thread: boundedElastic-5
         * next 3		: Thread: boundedElastic-8
         * next 6		: Thread: boundedElastic-2
         * next 2		: Thread: boundedElastic-9
         * next 5		: Thread: boundedElastic-7
         * next 9		: Thread: boundedElastic-1
         * next 4		: Thread: boundedElastic-4
         * sub 7		: Thread: boundedElastic-5
         * sub 2		: Thread: boundedElastic-5
         * sub 3		: Thread: boundedElastic-5
         * sub 4		: Thread: boundedElastic-5
         * sub 5		: Thread: boundedElastic-5
         * sub 6		: Thread: boundedElastic-5
         * sub 9		: Thread: boundedElastic-5
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");
    }

    private static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
