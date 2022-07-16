package com.example.sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05Delay {
    public static void main(String[] args) {
        Flux.range(1, 100)
            .log()
            .delayElements(Duration.ofSeconds(1)) // request(32) !!
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        /**
         * request(32)
         * onNext(1)
         * ..
         * onNext(32)
         */
        Util.sleepSeconds(60);
        /**
         * received: 1
         * received: 2
         * ...
         * received: 23
         * request(24) !!
         * onNext(33)
         * ...
         * onNext(56)
         * received: 24
         * ...
         * received: 47
         * request(24) !!
         * onNext(57)
         * ...
         * received: 48
         * ...
         */

        /**
         * reactor.util.concurrent
         * public final class Queues
         *     public static final int CAPACITY_UNSURE = Integer.MIN_VALUE;
         *     public static final int XS_BUFFER_SIZE = Math.max(8, Integer.parseInt(System.getProperty("reactor.bufferSize.x", "32")));
         *     public static final int SMALL_BUFFER_SIZE = Math.max(16, Integer.parseInt(System.getProperty("reactor.bufferSize.small", "256")));
         */
    }
}
