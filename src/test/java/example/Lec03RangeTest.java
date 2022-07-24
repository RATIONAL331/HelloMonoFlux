package example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec03RangeTest {
    @Test
    public void test1() {
        Flux<Integer> just = Flux.range(1, 50);

        just.as(StepVerifier::create)
            .expectNextCount(50)
            .verifyComplete();
    }

    @Test
    public void test2() {
        Flux<Integer> just = Flux.range(1, 50);

        just.as(StepVerifier::create)
            .thenConsumeWhile(i -> i < 100)
            .verifyComplete();
    }

    @Test
    public void test3() {
        Flux<Integer> just = Flux.range(1, 50).map(i -> i * 2);

        Assertions.assertThrows(AssertionError.class, () -> {
            just.as(StepVerifier::create)
                .thenConsumeWhile(i -> i < 100)
                .verifyComplete();
        });
    }
}
