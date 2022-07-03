package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec09FluxFromMono {
    public static void main(String[] args) {
        Mono<String> mono = Mono.just("a");
        // doSomething(mono);
        Flux<String> from = Flux.from(mono); // mono to flux
        doSomething(from);
        from.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("==============================================================");

        Flux<Integer> range = Flux.range(1, 10);
        range.next() // flux to mono (first item)
             .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

    }

    private static void doSomething(Flux<String> flux) {

    }
}
