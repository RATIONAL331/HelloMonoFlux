package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec05FluxRange {
    public static void main(String[] args) {
        /**
         * [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
         * [ INFO] (main) | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
         * [ INFO] (main) | request(unbounded) // unbounded mean just give all data you have; infinite amount of data that you have
         * [ INFO] (main) | request(unbounded)
         * [ INFO] (main) | onNext(3)
         * [ INFO] (main) | onNext(Lenny Langworth)
         * Received: Lenny Langworth
         * [ INFO] (main) | onNext(4)
         * [ INFO] (main) | onNext(Gema Koss MD)
         * Received: Gema Koss MD
         * [ INFO] (main) | onNext(5)
         * [ INFO] (main) | onNext(Nikita Paucek)
         * Received: Nikita Paucek
         * [ INFO] (main) | onNext(6)
         * [ INFO] (main) | onNext(Mrs. Lyn Bosco)
         * Received: Mrs. Lyn Bosco
         * [ INFO] (main) | onNext(7)
         * [ INFO] (main) | onNext(Mrs. Marcus Gorczany)
         * Received: Mrs. Marcus Gorczany
         * [ INFO] (main) | onComplete()
         * [ INFO] (main) | onComplete()
         * Completed
         */

        Flux<Integer> range = Flux.range(3, 5)
                                  .log();

        range.map(i -> Util.faker().name().fullName()) // subscribe too
             .log()
             .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("==============================================================");

        Flux.range(3, 1000)
            .map(i -> i / (i - 4)) // 4 divide by 0 and 5 not emit
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
