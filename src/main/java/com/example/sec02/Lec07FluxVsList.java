package com.example.sec02;

import com.example.sec02.helper.NameGenerator;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class Lec07FluxVsList {
    public static void main(String[] args) {
        List<String> names = NameGenerator.getNames(5); // wait 5 seconds
        System.out.println(names);

        Flux<String> nameFlux = NameGenerator.getNameFlux(5); // Results are visible as soon as they are generated
        nameFlux.subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }
}
