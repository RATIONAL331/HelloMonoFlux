package com.example.Sec04;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec03DoCallbacks {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
                System.out.println("inside create");
                for (int i = 0; i < 5; i++) {
                    fluxSink.next(i);
                }
                fluxSink.complete();
                System.out.println("create completed");
            })
            .doFirst(() -> System.out.println("doFirst 3"))
            .doFinally(signal -> System.out.println("doFinally 1" + signal))
            .doOnComplete(() -> System.out.println("doOnComplete"))
            .doFirst(() -> System.out.println("doFirst 1"))
            .doOnNext(i -> System.out.println("doOnNext: " + i))
            .doOnSubscribe(subscription -> System.out.println("doOnSubscribe 1" + subscription))
            .doOnRequest(request -> System.out.println("doOnRequest: " + request))
            .doOnEach(err -> System.out.println("doOnError: " + err))
            .doFirst(() -> System.out.println("doFirst 2"))
            .doOnTerminate(() -> System.out.println("doOnTerminate"))
            .doOnSubscribe(subscription -> System.out.println("doOnSubscribe 2" + subscription)) //
            .doOnCancel(() -> System.out.println("doOnCancel"))
            .doFinally(signal -> System.out.println("doFinally 2" + signal))
            .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard " + o))
            .take(2) // doOnDiscard 2, 3, 4
            .doFinally(signal -> System.out.println("doFinally 3" + signal))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        /**
         * doFirst 2
         * doFirst 1
         * doFirst 3 // bottom to top (subscriber executes bottom to top)
         * doOnSubscribe 1
         * doOnSubscribe 2 // top to bottom -> (subscription execute publisher to subscriber)
         * Received: 1
         * doOnCancel
         * doFinally 1cancel
         * doFinally 2cancel
         * Completed
         * doFinally 3onComplete // top to bottom -> (subscription execute publisher to subscriber)
         */
    }
}
