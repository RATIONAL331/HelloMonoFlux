package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec02HandleAssignment {
    public static void main(String[] args) {
        Flux.generate(synchronousSink -> {
                synchronousSink.next(Util.faker().country().name());
            })
            .map(Object::toString)
            .handle(((country, synchronousSink) -> {
                synchronousSink.next(country);
                if (country.equalsIgnoreCase("canada")) {
                    synchronousSink.complete(); // until
                }
            }))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
