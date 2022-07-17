package com.example.sec05;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Lec03HotPublish {
    public static void main(String[] args) {
        // share = publish().refCount(1);
        Flux<String> movieStream = Flux.fromStream(Lec03HotPublish::getMovie)
                                       .delayElements(Duration.ofMillis(500))
                                       .publish() // return ConnectableFlux
                                       .refCount(1);

        movieStream.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(1);
        movieStream.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub2; Received: Scene 3 <- notice
         * sub1; Completed
         * sub2; Completed
         */
        System.out.println("===========================");

        Flux<String> movieStream2 = Flux.fromStream(Lec03HotPublish::getMovie)
                                        .delayElements(Duration.ofMillis(500))
                                        .publish() // return ConnectableFlux
                                        .refCount(2); // when subscriber count is 2, publisher will start to emit data

        movieStream2.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(1);
        movieStream2.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub2; Received: Scene 1 <- notice
         * sub1; Received: Scene 2
         * sub2; Received: Scene 2
         * sub1; Received: Scene 3
         * sub2; Received: Scene 3
         * sub1; Completed
         * sub2; Completed
         */
        System.out.println("===========================");

        Flux<String> movieStream3 = Flux.fromStream(Lec03HotPublish::getMovie)
                                        .delayElements(Duration.ofMillis(500))
                                        .publish() // return ConnectableFlux
                                        .refCount(1);

        movieStream3.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(3);

        movieStream3.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub1; Completed
         * getMovie() <- notice
         * sub2; Received: Scene 1
         * sub2; Received: Scene 2
         * sub2; Received: Scene 3
         * sub2; Completed
         */
        System.out.println("===========================");

        Flux<String> movieStream4 = Flux.fromStream(Lec03HotPublish::getMovie)
                                        .delayElements(Duration.ofMillis(500))
                                        .share();

        movieStream4.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(3);

        movieStream4.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /** // same as above(publish().refCount(1))
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub1; Completed
         * getMovie()
         * sub2; Received: Scene 1
         * sub2; Received: Scene 2
         * sub2; Received: Scene 3
         * sub2; Completed
         */
        System.out.println("===========================");

        Flux<Object> flux = Flux.create(fluxSink -> {
                                    System.out.println("created");
                                    for (int i = 0; i < 5; i++) {
                                        fluxSink.next(i);
                                    }
                                    fluxSink.complete();
                                })
                                .publish()
                                .refCount(2);
        flux.subscribe(DefaultSubscriber.subscriber("sub1"));
        flux.subscribe(DefaultSubscriber.subscriber("sub2"));
        flux.subscribe(DefaultSubscriber.subscriber("sub3"));
        /**
         * created
         * sub1; Received: 0
         * sub2; Received: 0
         * sub1; Received: 1
         * sub2; Received: 1
         * sub1; Received: 2
         * sub2; Received: 2
         * sub1; Received: 3
         * sub2; Received: 3
         * sub1; Received: 4
         * sub2; Received: 4
         * sub1; Completed
         * sub2; Completed
         */ // sub3 is not received data

        System.out.println("===========================");


        Flux<Object> flux2 = Flux.create(fluxSink -> {
                                     System.out.println("created");
                                     for (int i = 0; i < 5; i++) {
                                         fluxSink.next(i);
                                     }
                                     fluxSink.complete();
                                 })
                                 .publish()
                                 .refCount(2);
        flux2.subscribe(DefaultSubscriber.subscriber("sub1"));
        flux2.subscribe(DefaultSubscriber.subscriber("sub2"));
        flux2.subscribe(DefaultSubscriber.subscriber("sub3"));
        flux2.subscribe(DefaultSubscriber.subscriber("sub4"));
        /**
         * created
         * sub1; Received: 0
         * sub2; Received: 0
         * sub1; Received: 1
         * sub2; Received: 1
         * sub1; Received: 2
         * sub2; Received: 2
         * sub1; Received: 3
         * sub2; Received: 3
         * sub1; Received: 4
         * sub2; Received: 4
         * sub1; Completed
         * sub2; Completed
         * created
         * sub3; Received: 0
         * sub4; Received: 0
         * sub3; Received: 1
         * sub4; Received: 1
         * sub3; Received: 2
         * sub4; Received: 2
         * sub3; Received: 3
         * sub4; Received: 3
         * sub3; Received: 4
         * sub4; Received: 4
         * sub3; Completed
         * sub4; Completed
         */
    }

    private static Stream<String> getMovie() {
        System.out.println("getMovie()");
        return Stream.of("Scene 1", "Scene 2", "Scene 3");
    }
}
