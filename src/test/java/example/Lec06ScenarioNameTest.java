package example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Lec06ScenarioNameTest {
    @Test
    public void test1() {
        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().scenarioName("alphabet-test");
        Assertions.assertThrows(AssertionError.class, () -> {
            Flux.just("a", "b", "c")
                .as(publisher -> StepVerifier.create(publisher, stepVerifierOptions))
                .expectNextCount(12)
                .verifyComplete();
        });
    }

    @Test
    public void test2() {
        Flux.just("a", "b", "c")
            .as(StepVerifier::create)
            .expectNext("a")
            .as("a-test")
            .expectNext("b", "c")
            .as("b,c-test")
            .verifyComplete();
    }
}
