package com.example.Sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec01Handle {
    public static void main(String[] args) {
        // handle = filter + map
        Flux.range(1, 20)
            .handle(((integer, synchronousSink) -> {
                // integer actually what emit
                if (integer % 2 == 0) {
                    synchronousSink.next(integer); // filter
                } else {
                    synchronousSink.next(integer + " is odd"); // map
                }
            }))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Flux.range(1, 20)
            .handle(((integer, synchronousSink) -> {
                // integer actually what emit
                if (integer == 7) synchronousSink.complete(); // until
                else synchronousSink.next(integer);
            }))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
