package com.example.sec08;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

public class Lec02Concat {
    public static void main(String[] args) {
        Flux<String> pub1 = Flux.just("a", "b", "c");
        Flux<String> pub2 = Flux.just("d", "e", "f");
        Flux<String> error = Flux.error(new RuntimeException());

        // Flux<String> stringFlux = pub1.concatWith(pub2);
        Flux<String> stringFlux = Flux.concat(pub1, pub2, error);

        stringFlux.subscribe(new DefaultSubscriber("subscriber1"));

        /**
         * subscriber1; Received: a
         * subscriber1; Received: b
         * subscriber1; Received: c
         * subscriber1; Received: d
         * subscriber1; Received: e
         * subscriber1; Received: f
         * subscriber1; Error: java.lang.RuntimeException
         */
        System.out.println("===============================");

        Flux<String> stringFlux2 = Flux.concat(pub1, error, pub2);
        stringFlux2.subscribe(new DefaultSubscriber("subscriber2"));
        /**
         * subscriber2; Received: a
         * subscriber2; Received: b
         * subscriber2; Received: c
         * subscriber2; Error: java.lang.RuntimeException
         */
        System.out.println("===============================");

        Flux<String> stringFlux3 = Flux.concatDelayError(pub1, error, pub2);
        stringFlux3.subscribe(new DefaultSubscriber("subscriber3"));

        /**
         * subscriber3; Received: a
         * subscriber3; Received: b
         * subscriber3; Received: c
         * subscriber3; Received: d <- notice
         * subscriber3; Received: e
         * subscriber3; Received: f
         * subscriber3; Error: java.lang.RuntimeException
         */

    }
}
