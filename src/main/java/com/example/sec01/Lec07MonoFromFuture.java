package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Lec07MonoFromFuture {
    public static void main(String[] args) {
        Mono<String> fromFuture = Mono.fromFuture(getName());
        fromFuture.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        // async pool of threads
        Util.sleepSeconds(1);
    }

    private static CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> Util.faker().name().fullName());
    }
}
