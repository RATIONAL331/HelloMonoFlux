package com.example.sec07;

import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class Lec02Drop_03 {
    public static void main(String[] args) {
        System.setProperty("reactor.bufferSize.small", "16");

        List<Object> list = new ArrayList<>();

        Flux.create(fluxSink -> {
                for (int i = 0; i < 500; i++) {
                    fluxSink.next(i);
                    System.out.println("emit " + i);
                    Util.sleepMillis(1); // <- 10 times fast
                }
                fluxSink.complete();
            })
            //.onBackpressureBuffer()
            .onBackpressureDrop(list::add) // <- consumer add if drop object
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(i -> Util.sleepMillis(10))
            .subscribe(new DefaultSubscriber("subscribe"));

        Util.sleepSeconds(3);
        System.out.println("print drop list");
        System.out.println(list);

        /**
         * subscribe; Received: 450
         * subscribe; Received: 451
         * subscribe; Received: 452
         * subscribe; Received: 453
         * subscribe; Completed
         * print drop list
         * [16, 17, 18, 19, 20, 21, 22, ...]
         */
    }
}
