package example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class Lec07ContextTest {
    private static Mono<String> getWelcomeMessage() {
        return Mono.deferContextual(context -> {
            if (context.hasKey("user")) {
                return Mono.just("Welcome " + context.get("user"));
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }

    @Test
    public void test1() {
        getWelcomeMessage().as(StepVerifier::create)
                           .verifyError(RuntimeException.class);
    }

    @Test
    public void test2() {
        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(Context.of("user", "sam"));

        getWelcomeMessage().as(publisher -> StepVerifier.create(publisher, stepVerifierOptions))
                           .expectNext("Welcome sam")
                           .verifyComplete();
    }
}
