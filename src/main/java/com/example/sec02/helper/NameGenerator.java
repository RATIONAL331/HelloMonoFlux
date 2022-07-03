package com.example.sec02.helper;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NameGenerator {

    public static List<String> getNames(int count) {
        return IntStream.range(0, count)
                        .mapToObj(i -> getName())
                        .collect(Collectors.toList());
    }

    public static Flux<String> getNameFlux(int count) {
        return Flux.range(0, count)
                   .map(i -> getName());
    }

    private static String getName() {
        System.out.println("Generate name...");
        Util.sleepSeconds(1);
        return Util.faker().name().fullName();
    }
}
