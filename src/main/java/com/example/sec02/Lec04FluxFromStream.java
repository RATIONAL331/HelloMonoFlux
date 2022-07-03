package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

public class Lec04FluxFromStream {
    public static void main(String[] args) {
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        Stream<Integer> stream = integers.stream();
        stream.forEach(System.out::println); // closed
//        stream.forEach(System.out::println); // IllegalStateException: stream has ALREADY been operated upon or closed
        System.out.println("============================================================");

        List<Integer> integerList = List.of(1, 2, 3, 4, 5);
        Stream<Integer> integerStream = integerList.stream();
        Flux<Integer> integerFlux = Flux.fromStream(integerStream);
        integerFlux.subscribe(i -> System.out.println("Subscriber 1: " + i)); // stream closed
//        integerFlux.subscribe(i -> System.out.println("Subscriber 2: " + i)); // IllegalStateException: stream has ALREADY been operated upon or closed
        System.out.println("============================================================");

        Flux<Integer> reusableFlux = Flux.fromStream(() -> integerList.stream());
        reusableFlux.subscribe(i -> System.out.println("Subscriber 1: " + i));
        reusableFlux.subscribe(i -> System.out.println("Subscriber 2: " + i));
    }
}
