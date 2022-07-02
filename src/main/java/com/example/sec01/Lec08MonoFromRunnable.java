package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;

public class Lec08MonoFromRunnable {
    public static void main(String[] args) {
        // no param & return
        Mono<Void> mono = Mono.fromRunnable(timeConsumingProcess());
        mono.subscribe(Util.onNext(), // no return so not consume
                       Util.onError(), // no error so not consume
                       () -> System.out.println("process is done. notify emails.")); // only complete consume

    }

    private static Runnable timeConsumingProcess() {
        return () -> {
            Util.sleepSeconds(3);
            System.out.println("Process Done!");
        };
    }
}
