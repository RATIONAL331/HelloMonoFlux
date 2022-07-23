package com.example.sec08.helper;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class AsianaFlights {
    public static Flux<String> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(1, 10))
                   .delayElements(Duration.ofSeconds(1))
                   .map(i -> "AAA: " + Util.faker().random().nextInt(100, 999))
                   .filter(i -> Util.faker().random().nextBoolean());
    }
}
