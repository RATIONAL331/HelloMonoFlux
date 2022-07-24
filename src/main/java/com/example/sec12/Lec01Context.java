package com.example.sec12;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Lec01Context {
    public static void main(String[] args) {
        Mono<String> welcomeMessage = getWelcomeMessage();
        Mono<String> welcomeMessageWithContext = getWelcomeMessage().contextWrite(Context.of("user", "sub"));

        welcomeMessage.subscribe(new DefaultSubscriber("subscriber"));
        welcomeMessageWithContext.subscribe(new DefaultSubscriber("subscriber"));
        /**
         * subscriber; Error: java.lang.RuntimeException: unauthenticated
         * subscriber; Received: Welcome sub
         * subscriber; Completed
         */

        System.out.println("===============================");

        Mono<String> welcomeMessageWithContext2 = getWelcomeMessage().contextWrite(Context.of("user", "sub1"))
                                                                     .contextWrite(Context.of("user", "sub2"));

        welcomeMessageWithContext2.subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: Welcome sub1 <- notice not sub2
         * subscriber; Completed
         */

        System.out.println("===============================");

        Mono<String> welcomeMessageWithContext3 = getWelcomeMessage().contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase()))
                                                                     .contextWrite(Context.of("user", "sub2"));


        welcomeMessageWithContext3.subscribe(new DefaultSubscriber("subscriber"));
        /**
         * subscriber; Received: Welcome SUB2
         * subscriber; Completed
         */
    }

    private static Mono<String> getWelcomeMessage() {
        return Mono.deferContextual(context -> {
            if (context.hasKey("user")) {
                return Mono.just("Welcome " + context.get("user"));
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }
}
