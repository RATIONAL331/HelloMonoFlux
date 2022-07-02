package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Lec05MonoFromSupplier {
    public static void main(String[] args) {
        System.out.println("==============================");
        // 'just' method only if you have data ALREADY!
        Mono<String> just = Mono.just(getName());
        System.out.println("==============================");

        // ON DEMAND
        Mono<String> stringMono = Mono.fromSupplier(() -> getName());
        System.out.println("==============================");
        Mono<String> stringMono2 = Mono.fromSupplier(() -> getName());
        stringMono2.subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        System.out.println("==============================");

        Supplier<String> stringSupplier = () -> getName();
        Mono.fromSupplier(stringSupplier).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        Callable<String> stringCallable = () -> getName();
        Mono.fromCallable(stringCallable).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        System.out.println("==============================");
    }
    private static String getName() {
        System.out.println("Generate name...");
        return Util.faker().name().fullName();
    }
}
