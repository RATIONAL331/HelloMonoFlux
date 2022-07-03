package com.example.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@RequiredArgsConstructor
public class DefaultSubscriber implements Subscriber<Object> {
    private final String name;

    public static Subscriber<Object> subscriber() {
        return subscriber(StringUtils.EMPTY);
    }

    public static Subscriber<Object> subscriber(String name) {
        return new DefaultSubscriber(name);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        System.out.println(name + "; Received: " + o);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(name + "; Error: " + throwable);
    }

    @Override
    public void onComplete() {
        System.out.println(name + "; Completed");
    }

}
