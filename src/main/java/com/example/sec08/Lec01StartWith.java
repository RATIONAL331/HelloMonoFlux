package com.example.sec08;

import com.example.sec08.helper.NameGenerator;
import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

public class Lec01StartWith {
    public static void main(String[] args) {
        NameGenerator.generateName()
                     .take(3)
                     .subscribe(new DefaultSubscriber("subscriber1"));

        System.out.println("===============================");

        NameGenerator.generateName()
                     .take(3)
                     .subscribe(new DefaultSubscriber("subscriber2"));

        System.out.println("===============================");

        NameGenerator.generateName()
                     .take(5)
                     .subscribe(new DefaultSubscriber("subscriber3"));

        System.out.println("===============================");

        NameGenerator.generateName()
                     .filter(n -> n.startsWith("A"))
                     .take(2)
                     .subscribe(new DefaultSubscriber("subscriber4"));

        System.out.println("===============================");

        /**
         * getFromCache
         * generateName
         * subscriber1; Received: Iola <- slow
         * generateName
         * subscriber1; Received: Nathaniel
         * generateName
         * subscriber1; Received: Apryl
         * subscriber1; Completed
         * ===============================
         * getFromCache
         * subscriber2; Received: Iola <- fast (startWith)
         * subscriber2; Received: Nathaniel
         * subscriber2; Received: Apryl
         * subscriber2; Completed
         * ===============================
         * getFromCache
         * subscriber3; Received: Iola <- fast
         * subscriber3; Received: Nathaniel
         * subscriber3; Received: Apryl
         * generateName
         * subscriber3; Received: Laurice <- slow
         * generateName
         * subscriber3; Received: Phuong
         * subscriber3; Completed
         * ===============================
         * getFromCache
         * subscriber4; Received: Apryl <- fast
         * generateName
         * generateName
         * ...
         * subscriber4; Received: Agustin <- slow
         * subscriber4; Completed
         */

        Flux<String> flux = Flux.just("a", "b", "c");
        flux.startWith(flux)
            .subscribe(new DefaultSubscriber("subscriber5"));

        /**
         * subscriber5; Received: a
         * subscriber5; Received: b
         * subscriber5; Received: c
         * subscriber5; Received: a
         * subscriber5; Received: b
         * subscriber5; Received: c
         * subscriber5; Completed
         */

    }
}
