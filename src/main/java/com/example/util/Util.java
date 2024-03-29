package com.example.util;

import com.github.javafaker.Faker;

import java.util.function.Consumer;

public class Util {

    private static final Faker FAKER = Faker.instance();
    public static Consumer<Object> onNext() {
        return o -> System.out.println("Received: " + o);
    }

    public static Consumer<Throwable> onError() {
        return e -> System.out.println("ERROR: " + e);
    }

    public static Runnable onComplete() {
        return () -> System.out.println("Completed");
    }

    public static Faker faker() {
        return FAKER;
    }

    public static void sleepSeconds(int second) {
        sleepMillis(second * 1000);
    }

    public static void sleepMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
