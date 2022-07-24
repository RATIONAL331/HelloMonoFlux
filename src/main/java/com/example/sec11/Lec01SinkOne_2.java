package com.example.sec11;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * SINK
 * one => Mono -> 1:N
 * many(unicast) => Flux -> 1:1
 * many(multicast) => Flux -> 1:N
 * many(replay) => Flux -> 1:N (with replay of all values to late subscribers)
 */
public class Lec01SinkOne_2 {
    public static void main(String[] args) {
        // mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        Mono<Object> objectMono = sink.asMono(); // subscribe to value
        objectMono.subscribe(new DefaultSubscriber("subscribe"));
        objectMono.subscribe(new DefaultSubscriber("subscribe2"));
        sink.tryEmitValue("hi"); // publish value <- notice emit value after subscribe
        /**
         * subscribe; Received: hi
         * subscribe; Completed
         * subscribe2; Received: hi
         * subscribe2; Completed
         */
    }
}
