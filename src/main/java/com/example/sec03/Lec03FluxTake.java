package com.example.sec03;

import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec03FluxTake {
    public static void main(String[] args) {
        Flux.range(1, 10)
            .log()
            .take(3) // call cancel() after 3rd element then no emit 4th element(find log don't call onNext(4))
            .log()
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("============================================================");

        Flux.range(1, 2)
            .log()
            .take(3)
            .log()
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
