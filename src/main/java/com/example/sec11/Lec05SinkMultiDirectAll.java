package com.example.sec11;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class Lec05SinkMultiDirectAll {
    public static void main(String[] args) {
        System.setProperty("reactor.bufferSize.small", "16");

        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().multicast().directAllOrNothing(); // <- notice directAllOrNothing (notify the caller with Sinks.EmitResult.FAIL_OVERFLOW if "ANY" of the subscribers cannot process an element)

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();

        flux.subscribe(new DefaultSubscriber("subscriber"));
        flux.delayElements(Duration.ofMillis(200)).subscribe(new DefaultSubscriber("subscriber2"));

        for (int i = 0; i < 100; i++) {
            sink.tryEmitNext(i);
        }

        /**
         * subscriber; Received: 0
         * ...
         * subscriber; Received: 30
         * subscriber; Received: 31 <- subscriber was affected by subscriber2(32 ~ 99 not received)
         * subscriber2; Received: 0 <- too slow!
         * ...
         * subscriber2; Received: 31
         */

        Util.sleepSeconds(10);
        System.out.println("===============================");

        // handle through which we would push items
        Sinks.Many<Object> sink2 = Sinks.many().multicast().directBestEffort(); // <- notice directBestEffort(notify the caller with Sinks.EmitResult.FAIL_OVERFLOW if "NONE" of the subscribers can process an element)

        // handle through which subscribers will receive items
        Flux<Object> flux2 = sink2.asFlux();

        flux2.subscribe(new DefaultSubscriber("subscriber"));
        flux2.delayElements(Duration.ofMillis(200)).subscribe(new DefaultSubscriber("subscriber2"));

        for (int i = 0; i < 100; i++) {
            sink2.tryEmitNext(i);
        }

        /**
         * subscriber; Received: 0
         * ...
         * subscriber; Received: 98
         * subscriber; Received: 99
         * subscriber2; Received: 0 <- too slow!
         * ...
         * subscriber2; Received: 31
         */

        Util.sleepSeconds(10);
    }
}
