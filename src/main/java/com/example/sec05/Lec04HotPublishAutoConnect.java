package com.example.sec05;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Lec04HotPublishAutoConnect { // not re-subscribe
    public static void main(String[] args) {
        Flux<String> movieStream = Flux.fromStream(Lec04HotPublishAutoConnect::getMovie)
                                       .delayElements(Duration.ofMillis(500))
                                       .publish()
                                       .autoConnect(1); // <- notice (refCount -> autoConnect) => there is no re-subscription

        movieStream.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(1);
        System.out.println("join sub2");
        movieStream.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * join sub2
         * sub1; Received: Scene 3
         * sub2; Received: Scene 3
         * sub1; Completed
         * sub2; Completed
         */

        System.out.println("==============================================================");

        Flux<String> movieStream2 = Flux.fromStream(Lec04HotPublishAutoConnect::getMovie)
                                        .delayElements(Duration.ofMillis(500))
                                        .publish()
                                        .autoConnect(1); // <- notice (refCount -> autoConnect)

        movieStream2.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(3);
        System.out.println("join sub2");
        movieStream2.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub1; Completed
         * join sub2
         * <- notice (sub2 not received any data)
         */
        System.out.println("==============================================================");

        Flux<String> movieStream3 = Flux.fromStream(Lec04HotPublishAutoConnect::getMovie)
                                        .delayElements(Duration.ofMillis(500))
                                        .publish()
                                        .autoConnect(0); // just start to emit data -> real hot publisher

        Util.sleepSeconds(1);
        movieStream3.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(2);
        System.out.println("join sub2");
        movieStream3.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * <- notice (sub1 not receive data 1)
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub1; Completed
         * join sub2
         */
    }

    private static Stream<String> getMovie() {
        System.out.println("getMovie()");
        return Stream.of("Scene 1", "Scene 2", "Scene 3");
    }
}
