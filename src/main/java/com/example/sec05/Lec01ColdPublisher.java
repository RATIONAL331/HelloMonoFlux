package com.example.sec05;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Lec01ColdPublisher {
    public static void main(String[] args) {
        Flux<String> movieStream = Flux.fromStream(Lec01ColdPublisher::getMovie)
                                       .delayElements(Duration.ofMillis(500));

        movieStream.subscribe(DefaultSubscriber.subscriber("sub1"));
        Util.sleepSeconds(1);
        movieStream.subscribe(DefaultSubscriber.subscriber("sub2"));
        Util.sleepSeconds(3);

        /**
         * getMovie()
         * sub1; Received: Scene 1
         * sub1; Received: Scene 2
         * getMovie() <- notice
         * sub1; Received: Scene 3
         * sub1; Completed
         * sub2; Received: Scene 1
         * sub2; Received: Scene 2
         * sub2; Received: Scene 3
         * sub2; Completed
         */
    }

    private static Stream<String> getMovie() {
        System.out.println("getMovie()");
        return Stream.of("Scene 1", "Scene 2", "Scene 3");
    }
}
