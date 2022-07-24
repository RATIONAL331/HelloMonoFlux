package com.example.sec09;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec02OverlapAndDrop {
    public static void main(String[] args) {
        eventStream().buffer(3, 1) // dropping buffer
                     //.buffer(3) equals buffer(3, 3)
                     .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: [event0, event1, event2]
         * subscriber; Received: [event1, event2, event3]
         * subscriber; Received: [event2, event3, event4]
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");

        eventStream().buffer(3, 2)
                     .subscribe(new DefaultSubscriber("subscriber2"));

        /**
         * subscriber2; Received: [event0, event1, event2]
         * subscriber2; Received: [event2, event3, event4] <- notice event0, event1 removed
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");


        eventStream().buffer(3, 5)
                     .subscribe(new DefaultSubscriber("subscriber3"));

        /**
         * subscriber3; Received: [event0, event1, event2]
         * subscriber3; Received: [event5, event6, event7]
         */

        Util.sleepSeconds(3);
        System.out.println("===============================");
    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                   .map(i -> "event" + i);
    }
}
