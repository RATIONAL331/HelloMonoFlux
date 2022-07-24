package com.example.sec11;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec06Replay {
    public static void main(String[] args) {
        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().replay().all(/* size if empty => all */);

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("hi2");

        flux.subscribe(new DefaultSubscriber("subscriber"));
        flux.subscribe(new DefaultSubscriber("subscriber2"));
        sink.tryEmitNext("hi3");

        flux.subscribe(new DefaultSubscriber("subscriber3"));
        sink.tryEmitNext("hi4");

        /**
         * subscriber; Received: hi
         * subscriber; Received: hi2
         * subscriber2; Received: hi
         * subscriber2; Received: hi2
         * subscriber; Received: hi3
         * subscriber2; Received: hi3
         * subscriber3; Received: hi <- notice subscriber3 is received hi, hi2
         * subscriber3; Received: hi2 <- notice subscriber3 is received hi, hi2
         * subscriber3; Received: hi3
         * subscriber; Received: hi4
         * subscriber2; Received: hi4
         * subscriber3; Received: hi4
         */
    }
}
