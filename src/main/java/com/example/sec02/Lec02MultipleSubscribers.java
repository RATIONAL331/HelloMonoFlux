package com.example.sec02;

import reactor.core.publisher.Flux;

public class Lec02MultipleSubscribers {
    public static void main(String[] args) {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> even = just.filter(i -> i % 2 == 0);
        just.subscribe(i -> System.out.println("Subscriber 1: " + i));
        just.subscribe(i -> System.out.println("Subscriber 2: " + i));
        even.subscribe(i -> System.out.println("Subscriber 3: " + i));
    }
}
