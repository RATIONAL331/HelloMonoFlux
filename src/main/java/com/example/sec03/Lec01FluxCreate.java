package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec01FluxCreate {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
            // emit what you provided
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.complete();
        }).subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("============================================================");

        Flux.create(fluxSink -> {
            String country;
            do {
                country = Util.faker().address().country();
                fluxSink.next(country);
            } while (!country.toLowerCase().startsWith("canada")); // -> doesn't check cancel; so when cancel flux pipeline emit data not cancel
            fluxSink.complete();
        }).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
