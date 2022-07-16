package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec04LimitRate {
    public static void main(String[] args) {
        Flux.range(1, 1000)
            .log()
            .limitRate(100) // constantly 75%
            //.limitRate(100, 99) // 99%
            //.limitRate(100, 100) // 75% => implement if same or bigger 75%
            //.limitRate(100, 0) // 100%
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        /**
         * request(100)
         * onNext(1)
         * Received: 1
         * onNext(2)
         * Received: 2
         * ...
         * onNext(75)
         * received: 75
         * request(75)
         * onNext(76)
         * Received: 76
         * ...
         * request(75)
         * ...
         * request(75)
         * onNext(976)
         * Received: 976
         * ...
         * onNext(1000)
         * Received: 1000
         * onComplete()
         */
    }
}
