package com.example.sec08;

import com.example.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

public class Lec04Zip {
    public static void main(String[] args) {
        Flux.zip(getBody(), getTire(), getEngine())
            .subscribe(new DefaultSubscriber("subscriber"));
    }

    private static Flux<String> getBody() {
        return Flux.range(1, 5).map(i -> "body" + i);
    }

    private static Flux<String> getEngine() {
        return Flux.range(1, 2).map(i -> "engine" + i);
    }

    private static Flux<String> getTire() {
        return Flux.range(1, 6).map(i -> "tire" + i);
    }
}
