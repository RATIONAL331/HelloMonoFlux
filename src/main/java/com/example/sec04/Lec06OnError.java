package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec06OnError {
    public static void main(String[] args) {
        Flux.range(1, 10)
            .log()
            .map(i -> 10 / (5 - i))
            //.onErrorReturn(-1) // still cancel
            //.onErrorResume(e -> fallback()) // still cancel
            .onErrorContinue((err, obj) -> {
                System.out.println("onErrorContinue_e: " + err.getMessage());
                System.out.println("onErrorContinue_o: " + obj);
            }) // not cancel
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }

    private static Mono<Integer> fallback() {
        return Mono.fromSupplier(() -> Util.faker().random().nextInt(100, 200));
    }
}
