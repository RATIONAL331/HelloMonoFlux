package com.example.sec05;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Lec05HotPublishCache {
    public static void main(String[] args) {
        // cache = publish().replay(); => replay(INTEGER.MAX_VALUE)
        Flux<String> movieStream = Flux.fromStream(Lec05HotPublishCache::getMovie)
                                       .delayElements(Duration.ofMillis(300))
                                       .cache(); // cache INTEGER.MAX_VALUE element

        movieStream.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(3);
        System.out.println("join sub2");
        movieStream.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(1);

        /**
         * getMovie()
         * sub1; Received: Scene 1 (300ms)
         * sub1; Received: Scene 2 (300ms)
         * sub1; Received: Scene 3 (300ms)
         * sub1; Completed
         * join sub2
         * sub2; Received: Scene 1 [ this process really quick! ] <- notice
         * sub2; Received: Scene 2 [ this process really quick! ]
         * sub2; Received: Scene 3 [ this process really quick! ]
         * sub2; Completed
         */
        System.out.println("==============================================================");

        Flux<String> movieStream2 = Flux.fromStream(Lec05HotPublishCache::getMovie)
                                        .delayElements(Duration.ofMillis(300))
                                        .cache(2); // only last 2 element

        movieStream2.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(3);
        System.out.println("join sub2");
        movieStream2.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(1);
        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * sub1; Received: Scene 3
         * sub1; Completed
         * join sub2
         * sub2; Received: Scene 2 <- notice
         * sub2; Received: Scene 3
         * sub2; Completed
         */
        System.out.println("==============================================================");

        Flux<Integer> flux = Flux.create(fluxSink -> {
            System.out.println("created");
            for (int i = 0; i < 5; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        });
        Flux<Integer> cache = flux.filter(i -> i > 1).cache(1);

        cache.subscribe(DefaultSubscriber.subscriber("sub1"));
        cache.subscribe(DefaultSubscriber.subscriber("sub2"));

        /**
         * created
         * sub1; Received: 2
         * sub1; Received: 3
         * sub1; Received: 4
         * sub1; Completed
         * sub2; Received: 4
         * sub2; Completed
         */
    }

    private static Stream<String> getMovie() {
        System.out.println("getMovie()");
        return Stream.of("Scene 1", "Scene 2", "Scene 3");
    }
}