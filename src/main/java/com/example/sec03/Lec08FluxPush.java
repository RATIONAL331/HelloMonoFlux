package com.example.sec03;

import com.example.sec03.helper.NameProducer;
import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec08FluxPush {
    public static void main(String[] args) {
        NameProducer nameProducer = new NameProducer();
        Flux.push(nameProducer) // push is not thread safe; only single thread can push
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Runnable runnable = () -> nameProducer.produce();
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}
