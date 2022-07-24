package example;

import com.example.sec09.helper.BookOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec04DelayTest {
    @Test
    public void test1() {
        Mono<BookOrder> mono = Mono.fromSupplier(BookOrder::new);
        mono.as(StepVerifier::create)
            .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
            .verifyComplete();
    }

    @Test
    public void test2() {
        Mono<BookOrder> mono = Mono.fromSupplier(BookOrder::new).delayElement(Duration.ofSeconds(3));
        mono.as(StepVerifier::create)
            .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
            .verifyComplete();
    }

    @Test
    public void test3() {
        Mono<BookOrder> mono = Mono.fromSupplier(BookOrder::new).delayElement(Duration.ofSeconds(3));
        mono.as(StepVerifier::create)
            .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
            .expectComplete()
            .verify(Duration.ofSeconds(5));
    }

    @Test
    public void test4() {
        Mono<BookOrder> mono = Mono.fromSupplier(BookOrder::new).delayElement(Duration.ofSeconds(3));

        Assertions.assertThrows(AssertionError.class, () -> {
            mono.as(StepVerifier::create)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .expectComplete()
                .verify(Duration.ofSeconds(1));
        });
    }
}
