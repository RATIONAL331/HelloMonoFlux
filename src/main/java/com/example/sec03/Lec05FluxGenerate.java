package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec05FluxGenerate {
    public static void main(String[] args) {
        // generate method doesn't need to loop
        Flux.generate(synchronousSink -> {
                System.out.println("emit");
                synchronousSink.next(Util.faker().country().name());
            })
            .take(5)
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("============================================================");

        Flux.generate(synchronousSink -> {
                System.out.println("emit");
                synchronousSink.next(Util.faker().country().name());
                synchronousSink.complete();
            })
            .take(5)
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("============================================================");

        Flux.generate(synchronousSink -> {
                System.out.println("emit");
                synchronousSink.next(Util.faker().country().name());
                synchronousSink.next(Util.faker().country().name()); // synchronousSink permit only 1 item emit
            })
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

//        Flux.generate(synchronousSink -> {
//                System.out.println("emit");
//                synchronousSink.next(Util.faker().country().name());
////                synchronousSink.next(Util.faker().country().name()); // synchronousSink permit only 1 item emit
//            })
//            .subscribe(Util.onNext(), Util.onError(), Util.onComplete()); // infinite loop
    }
}
