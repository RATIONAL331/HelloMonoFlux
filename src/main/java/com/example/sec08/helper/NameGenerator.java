package com.example.sec08.helper;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class NameGenerator {
    private static final List<String> list = new ArrayList<>();

    public static Flux<String> generateName() {
        return Flux.generate(stringSynchronousSink -> {
                       System.out.println("generateName");
                       Util.sleepSeconds(1);
                       String name = Util.faker().name().firstName();
                       list.add(name);
                       stringSynchronousSink.next(name);
                   })
                   .cast(String.class)
                   .startWith(getFromCache()); // <- start first
    }

    private static Flux<String> getFromCache() {
        System.out.println("getFromCache");
        return Flux.fromIterable(list);
    }
}
