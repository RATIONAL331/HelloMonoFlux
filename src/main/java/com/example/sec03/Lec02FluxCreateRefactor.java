package com.example.sec03;

import com.example.sec03.helper.NameProducer;
import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec02FluxCreateRefactor {
    public static void main(String[] args) {
        NameProducer nameProducer = new NameProducer();
        Flux.create(nameProducer) // fluxSink can multiple thread
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Runnable runnable = () -> nameProducer.produce();
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}
