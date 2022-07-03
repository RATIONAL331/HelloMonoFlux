package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec06FluxGenerateAssignment {
    public static void main(String[] args) {
        Flux.generate(synchronousSink -> {
                System.out.println("emit");
                String country = Util.faker().address().country();
                synchronousSink.next(country);
                if (country.toLowerCase().startsWith("canada")) {
                    synchronousSink.complete();
                }
            })
            .takeUntil(country -> ((String) country).toLowerCase().startsWith("canada"))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("============================================================");

        Flux.generate(synchronousSink -> {
                System.out.println("emit");
                synchronousSink.next(Util.faker().address().country());
            })
            .takeUntil(country -> ((String) country).toLowerCase().startsWith("canada"))
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
