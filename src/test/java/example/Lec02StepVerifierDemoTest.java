package example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec02StepVerifierDemoTest {
    @Test
    public void test1() {
        Flux<Integer> just = Flux.just(1, 2, 3);
        Flux<Object> error = Flux.error(new RuntimeException("error"));
        Flux<Object> concat = Flux.concat(just, error);

        concat.as(StepVerifier::create)
              .expectNext(1, 2, 3)
              .verifyError(RuntimeException.class);
    }

    @Test
    public void test2() {
        Flux<Integer> just = Flux.just(1, 2, 3);
        Flux<Object> error = Flux.error(new RuntimeException("error"));
        Flux<Object> concat = Flux.concat(just, error);

        concat.as(StepVerifier::create)
              .expectNext(1, 2, 3)
              .verifyErrorMessage("error");
    }

    @Test()
    public void test3() {
        Flux<Integer> just = Flux.just(1, 2, 3);
        Flux<Object> error = Flux.error(new RuntimeException("error"));
        Flux<Object> concat = Flux.concat(just, error);

        Assertions.assertThrows(AssertionError.class, () -> {
            concat.as(StepVerifier::create)
                  .expectNext(1, 2, 3)
                  .verifyError(IllegalArgumentException.class);
        });
    }
}
