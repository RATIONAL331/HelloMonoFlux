package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.io.Serializable;

public class Lec01FluxIntro {
    public static void main(String[] args) {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5);
        just.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Flux<Object> any = Flux.just(1, 2, "A4", Util.faker().name().fullName());
        any.subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
