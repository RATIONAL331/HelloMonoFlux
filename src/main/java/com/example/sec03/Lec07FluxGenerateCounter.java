package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec07FluxGenerateCounter {
    public static void main(String[] args) {
        Flux.generate(() -> 1, // initial state
                      (counter, sink) -> {
                          String name = Util.faker().country().name();
                          sink.next(name);
                          if (counter >= 10 || name.toLowerCase().startsWith("canada")) {
                              sink.complete();
                          }
                          return counter + 1;
                      })
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
