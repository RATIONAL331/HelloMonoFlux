package com.example.sec11;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class Lec01SinkOne_1 {
    public static void main(String[] args) {
        // mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one();
        Mono<Object> objectMono = sink.asMono(); // subscribe to value
        objectMono.subscribe(new DefaultSubscriber("subscribe"));
        sink.tryEmitValue("hi"); // publish value <- notice emit value after subscribe

        /**
         * subscribe; Received: hi
         * subscribe; Completed
         */

        System.out.println("===============================");

        Sinks.One<Object> sink2 = Sinks.one();
        Mono<Object> objectMono2 = sink2.asMono(); // subscribe to value
        objectMono2.subscribe(new DefaultSubscriber("subscribe2"));
        sink2.emitValue("hi", ((signalType, emitResult) -> {
            System.out.println("SIG TYPE: " + signalType.name()); // not execute (because only when error occurred)
            System.out.println("RES: " + emitResult.name());
            return false;
        }));
        sink2.emitValue("hello", ((signalType, emitResult) -> { // sink.one only permit one value emit
            System.out.println("SIG TYPE: " + signalType.name());
            System.out.println("RES: " + emitResult.name());
            // return true; <- retry until success (in this case infinite loop)
            return false;
        }));

        /**
         * subscribe2; Received: hi
         * subscribe2; Completed
         * SIG TYPE: ON_NEXT
         * RES: FAIL_TERMINATED
         */
    }
}
