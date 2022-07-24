package com.example.sec09;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec01Buffer {
    public static void main(String[] args) {
        eventStream().buffer(5)
                     .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: [event0, event1, event2, event3, event4]
         * subscriber; Received: [event5, event6, event7, event8, event9]
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");


        eventStream2().buffer(5)
                      .subscribe(new DefaultSubscriber("subscriber2"));

        /**
         * subscriber2; Received: [event0, event1, event2]
         * subscriber2; Completed
         */

        Util.sleepSeconds(1);
        System.out.println("===============================");

        eventStream3().buffer(Duration.ofSeconds(2))
                      .subscribe(new DefaultSubscriber("subscriber3"));

        /**
         * subscriber3; Received: [event0, event1, event2, event3, event4....]
         * subscriber3; Received: [event199, event200, event201...]
         */

        Util.sleepSeconds(5);
        System.out.println("===============================");

        eventStream3().bufferTimeout(5, Duration.ofSeconds(2)) // like or option
                      .subscribe(new DefaultSubscriber("subscriber4"));

        /**
         * subscriber4; Received: [event0, event1, event2, event3, event4]
         * subscriber4; Received: [event5, event6, event7, event8, event9]
         * ...
         * subscriber4; Received: [event295, event296, event297, event298, event299]
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");

        eventStream4().bufferTimeout(5, Duration.ofSeconds(2)) // like or option
                      .subscribe(new DefaultSubscriber("subscriber5"));

        /**
         * subscriber5; Received: [event0, event1, event2]
         * ...
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");

    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                   .map(i -> "event" + i);
    }

    private static Flux<String> eventStream2() {
        return Flux.interval(Duration.ofMillis(300))
                   .take(3)
                   .map(i -> "event" + i);
    }

    private static Flux<String> eventStream3() {
        return Flux.interval(Duration.ofMillis(10))
                   .map(i -> "event" + i);
    }

    private static Flux<String> eventStream4() {
        return Flux.interval(Duration.ofMillis(800))
                   .map(i -> "event" + i);
    }
}
