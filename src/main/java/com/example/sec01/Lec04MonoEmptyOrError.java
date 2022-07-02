package com.example.sec01;

import com.example.util.Util;
import reactor.core.publisher.Mono;

public class Lec04MonoEmptyOrError {
    public static void main(String[] args) {
        userRepository(1).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        System.out.println("==============================");
        userRepository(2).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        System.out.println("==============================");
        userRepository(3).subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }

    private static Mono<String> userRepository(int userId) {
        if (userId == 1) {
            return Mono.just(Util.faker().name().firstName());
        } else if(userId == 2) {
            return Mono.empty();
        } else {
            return Mono.error(new IllegalArgumentException("Not in the allowed range."));
        }
    }
}
