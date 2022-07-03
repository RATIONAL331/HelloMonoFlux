package com.example.sec02;

import com.example.util.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

public class Lec06Subscription {
    public static void main(String[] args) {
        AtomicReference<Subscription> subscriptionAtomicReference = new AtomicReference<>();
        Flux.range(1, 20)
            .log()
            .subscribeWith(new Subscriber<Integer>() {
                // publisher give subscription to subscriber
                @Override
                public void onSubscribe(Subscription subscription) {
                    System.out.println("Receiving subscription: " + subscription);
                    subscriptionAtomicReference.set(subscription);
                }

                @Override
                public void onNext(Integer integer) {
                    System.out.println("Receiving next: " + integer);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Receiving error: " + throwable);
                }

                @Override
                public void onComplete() {
                    System.out.println("Receiving complete");
                }
            });
        System.out.println("When you use custom Subscriber, you must designate subscription behavior.");
        System.out.println("============================================================");
        Util.sleepSeconds(1);
        Subscription subscription = subscriptionAtomicReference.get();
        subscription.request(2);
        System.out.println("============================================================");
        Util.sleepSeconds(1);
        subscription.request(5);
        System.out.println("============================================================");
        Util.sleepSeconds(1);
        System.out.println("subscription.cancel()");
        subscription.cancel();
        System.out.println("============================================================");
        Util.sleepSeconds(1);
        subscription.request(1);
        System.out.println("nothing happen");
    }
}
