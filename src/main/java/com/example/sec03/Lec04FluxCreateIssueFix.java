package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec04FluxCreateIssueFix {
    public static void main(String[] args) {
        // only one instance of fluxSink
        Flux.create(fluxSink -> {
                String country;
                do {
                    country = Util.faker().address().country();
                    System.out.println("emitting: " + country);
                    fluxSink.next(country);
                } while (!country.toLowerCase().startsWith("canada") && !fluxSink.isCancelled());
                fluxSink.complete();
            })
            .take(3)
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
