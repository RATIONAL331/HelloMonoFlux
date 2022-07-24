package com.example.sec11;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec02SinkUnicast {
    public static void main(String[] args) {
        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();
        flux.subscribe(new DefaultSubscriber("subscriber"));
        flux.subscribe(new DefaultSubscriber("subscriber2"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("hi2");
        sink.tryEmitNext("hi3");

        /**
         * subscriber2; Error: java.lang.IllegalStateException: UnicastProcessor allows only a single Subscriber
         * subscriber; Received: hi
         * subscriber; Received: hi2
         * subscriber; Received: hi3
         */
    }
}
