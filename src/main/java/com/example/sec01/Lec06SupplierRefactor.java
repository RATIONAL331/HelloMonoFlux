package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Lec06SupplierRefactor {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            // not block
            getName(); // just building pipeline
        }
        getName();
        getName().subscribe(Util.onNext(), Util.onError(), Util.onComplete()); // execute pipeline => Time consuming
        System.out.println("==============================");
        getName().subscribeOn(Schedulers.boundedElastic()) // async
                 .subscribeOn(Schedulers.parallel()).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        System.out.println("quickly print");
        // when sleep async function generating name completely.
        Util.sleepSeconds(4);
        System.out.println("==============================");
        String block = getName().subscribeOn(Schedulers.boundedElastic())
                                .block(); // block [NOT ACTUAL WAY]
        System.out.println(block);
    }

    private static Mono<String> getName() {
        System.out.println("Entered getName method.");
        return Mono.fromSupplier(() -> {
            System.out.println("Generate name...");
            Util.sleepSeconds(3);
            return Util.faker().name().fullName();
        }).map(String::toUpperCase);
    }
}
