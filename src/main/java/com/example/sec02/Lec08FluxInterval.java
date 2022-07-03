package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec08FluxInterval {
    public static void main(String[] args) {
        Flux.interval(Duration.ofSeconds(1)) // infinite
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Util.sleepSeconds(5);
    }
}
