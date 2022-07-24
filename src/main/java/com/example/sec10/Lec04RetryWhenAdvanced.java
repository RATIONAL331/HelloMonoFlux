package com.example.sec10;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class Lec04RetryWhenAdvanced {
    public static void main(String[] args) {
        orderService(Util.faker().business().creditCardNumber()).doOnError(err -> System.out.println("ERR: " + err))
                                                                .retry(5)
                                                                .subscribe(new DefaultSubscriber("subscribe"));
        /**
         * subscribe; Received: 524-19-1032
         * subscribe; Completed
         */

        /**
         * ERR: java.lang.RuntimeException: 404
         * ERR: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 500
         * subscribe; Received: 897-42-4384
         * subscribe; Completed
         */

        /**
         * ERR: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 404
         * ERR: java.lang.RuntimeException: 404
         * ERR: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 500
         * subscribe; Error: java.lang.RuntimeException: 500
         */
        System.out.println("===========================================================");

        orderService(Util.faker().business().creditCardNumber()).doOnError(err -> System.out.println("ERR: " + err))
                                                                .retryWhen(Retry.from(flux -> flux.doOnNext(rs -> {
                                                                                                      System.out.println("totalRetries: " + rs.totalRetries());
                                                                                                      System.out.println("failure: " + rs.failure());
                                                                                                  })
                                                                                                  .handle((rs, sink) -> {
                                                                                                      if (rs.failure().getMessage().startsWith("500")) {
                                                                                                          sink.next(1); // when sink emit ANY value, retry
                                                                                                      } else {
                                                                                                          sink.error(rs.failure()); // no more proceeding
                                                                                                      }
                                                                                                  })
                                                                                                  .delayElements(Duration.ofSeconds(1))))
                                                                .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(5);

        /**
         * subscribe; Received: 449-58-5414
         * subscribe; Completed
         */

        /**
         * ERR: java.lang.RuntimeException: 404
         * totalRetries: 0
         * failure: java.lang.RuntimeException: 404
         * subscribe; Error: java.lang.RuntimeException: 404
         */

        /**
         * ERR: java.lang.RuntimeException: 500
         * totalRetries: 0
         * failure: java.lang.RuntimeException: 500
         * subscribe; Received: 449-58-5414
         * subscribe; Completed
         */

        /**
         * ERR: java.lang.RuntimeException: 500
         * totalRetries: 0
         * failure: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 500
         * totalRetries: 1
         * failure: java.lang.RuntimeException: 500
         * ERR: java.lang.RuntimeException: 404
         * totalRetries: 2
         * failure: java.lang.RuntimeException: 404
         * subscribe; Error: java.lang.RuntimeException: 404
         */

    }

    // order service
    private static Mono<String> orderService(String cardNumber) {
        return Mono.fromSupplier(() -> {
            processPayment(cardNumber);
            return Util.faker().idNumber().valid();
        });
    }

    // payment service
    private static void processPayment(String cardNumber) {
        int random = Util.faker().random().nextInt(1, 10);
        if (random < 8) {
            throw new RuntimeException("500");
        } else if (random < 10) {
            throw new RuntimeException("404");
        }
    }
}
