package com.example.sec02;

import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class Lec03FluxFromArrayOrList {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "b", "c", "d", "e");
        Flux.fromIterable(strings)
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Integer[] arr = {1, 2, 3, 4, 5};
        Flux.fromArray(arr)
            .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
