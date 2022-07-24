package com.example.sec11;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec04SinkMulti {
    public static void main(String[] args) {
        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();

        flux.subscribe(new DefaultSubscriber("subscriber"));
        flux.subscribe(new DefaultSubscriber("subscriber2"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("hi2");
        sink.tryEmitNext("hi3");

        /**
         * subscriber; Received: hi
         * subscriber2; Received: hi
         * subscriber; Received: hi2
         * subscriber2; Received: hi2
         * subscriber; Received: hi3
         * subscriber2; Received: hi3
         */
        System.out.println("===============================");

        // handle through which we would push items
        Sinks.Many<Object> sink2 = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux2 = sink2.asFlux();

        flux2.subscribe(new DefaultSubscriber("subscriber"));

        sink2.tryEmitNext("hi");
        sink2.tryEmitNext("hi2");

        flux2.subscribe(new DefaultSubscriber("subscriber2"));
        sink2.tryEmitNext("hi3");

        /**
         * subscriber; Received: hi
         * subscriber; Received: hi2
         * subscriber; Received: hi3
         * subscriber2; Received: hi3 <- notice subscriber2 is not receiving hi, hi2
         */
        System.out.println("===============================");

        // handle through which we would push items
        Sinks.Many<Object> sink3 = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux3 = sink3.asFlux();

        sink3.tryEmitNext("hi");
        sink3.tryEmitNext("hi2");

        flux3.subscribe(new DefaultSubscriber("subscriber"));
        flux3.subscribe(new DefaultSubscriber("subscriber2"));
        sink3.tryEmitNext("hi3");
        flux3.subscribe(new DefaultSubscriber("subscriber3"));
        sink3.tryEmitNext("hi4");

        /**
         * subscriber; Received: hi <- notice subscriber received hi(because publisher emit the data when subscriber was present)
         * subscriber; Received: hi2
         * subscriber; Received: hi3
         * subscriber2; Received: hi3
         * subscriber; Received: hi4
         * subscriber2; Received: hi4
         * subscriber3; Received: hi4
         */

        System.out.println("===============================");

        // handle through which we would push items
        Sinks.Many<Object> sink4 = Sinks.many().multicast().directAllOrNothing(); // <- notice directAllOrNothing

        // handle through which subscribers will receive items
        Flux<Object> flux4 = sink4.asFlux();

        sink4.tryEmitNext("hi");
        sink4.tryEmitNext("hi2");

        flux4.subscribe(new DefaultSubscriber("subscriber"));
        flux4.subscribe(new DefaultSubscriber("subscriber2"));
        sink4.tryEmitNext("hi3");
        flux4.subscribe(new DefaultSubscriber("subscriber3"));
        sink4.tryEmitNext("hi4");

        /**
         * subscriber; Received: hi3 <- notice subscriber not received hi, hi2
         * subscriber2; Received: hi3
         * subscriber; Received: hi4
         * subscriber2; Received: hi4
         * subscriber3; Received: hi4
         */
    }
}
