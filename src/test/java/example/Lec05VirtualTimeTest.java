package example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec05VirtualTimeTest {
    @Test
    public void test1() {
        Flux<String> stringFlux = timeConsumingFlux();
        stringFlux.as(StepVerifier::create)
                  .expectNext("1a", "2a", "3a", "4a")
                  .verifyComplete();
    }

    @Test
    public void test2() {
        StepVerifier.withVirtualTime(this::timeConsumingFlux)
                    .thenAwait(Duration.ofSeconds(30))
                    .expectNext("1a", "2a", "3a", "4a")
                    .verifyComplete();
    }

    @Test
    public void test3() {
        StepVerifier.withVirtualTime(this::timeConsumingFlux)
                    .expectSubscription()
                    .expectNoEvent(Duration.ofSeconds(4))
                    .thenAwait(Duration.ofSeconds(20))
                    .expectNext("1a", "2a", "3a", "4a")
                    .verifyComplete();
    }

    @Test
    public void test4() {
        Assertions.assertThrows(AssertionError.class, () -> {
            StepVerifier.withVirtualTime(this::timeConsumingFlux)
                        .expectSubscription()
                        .expectNoEvent(Duration.ofSeconds(6))
                        .thenAwait(Duration.ofSeconds(20))
                        .expectNext("1a", "2a", "3a", "4a")
                        .verifyComplete();
        });
    }

    private Flux<String> timeConsumingFlux() {
        return Flux.range(1, 4)
                   .delayElements(Duration.ofSeconds(1))
                   .map(i -> i + "a");
    }
}
