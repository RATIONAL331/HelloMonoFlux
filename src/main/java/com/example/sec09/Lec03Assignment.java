package com.example.sec09;

import com.example.sec09.helper.BookOrder;
import com.example.sec09.helper.RevenueReport;
import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Lec03Assignment {
    public static void main(String[] args) {
        Set<String> allowedCategories = Set.of("Science fiction", "Fantasy", "Suspense/Thriller");

        bookStream().filter(book -> allowedCategories.contains(book.getCategory()))
                    .buffer(Duration.ofSeconds(2))
                    .doOnNext(books -> System.out.println("Received: " + books))
                    .map(Lec03Assignment::revenueCalculator)
                    .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * Received: [BookOrder(title=As I Lay Dying, author=Roman Schmitt, category=Science fiction, price=3.39), BookOrder(title=To a God Unknown, author=Abe Kshlerin, category=Science fiction, price=54.32)]
         * subscriber; Received: RevenueReport(localDateTime=2022-07-24T15:31:23.199543, revenue={Science fiction=57.71})
         * Received: [BookOrder(title=Fair Stood the Wind for France, author=Eddie Abshire Jr., category=Fantasy, price=41.84)]
         * subscriber; Received: RevenueReport(localDateTime=2022-07-24T15:31:27.186105, revenue={Fantasy=41.84})
         * Received: [BookOrder(title=A Passage to India, author=Mariana Connelly, category=Fantasy, price=87.83), BookOrder(title=The Monkey's Raincoat, author=Leonila Rippin II, category=Science fiction, price=18.53)]
         * subscriber; Received: RevenueReport(localDateTime=2022-07-24T15:31:29.186191, revenue={Science fiction=18.53, Fantasy=87.83})
         */

        Util.sleepSeconds(10);
    }

    private static RevenueReport revenueCalculator(List<BookOrder> bookOrderList) {
        Map<String, Double> totalPriceByCategory = bookOrderList.stream()
                                                                .collect(Collectors.groupingBy(BookOrder::getCategory,
                                                                                               Collectors.summingDouble(BookOrder::getPrice)));
        return new RevenueReport(totalPriceByCategory);
    }

    private static Flux<BookOrder> bookStream() {
        return Flux.interval(Duration.ofMillis(200))
                   .map(i -> new BookOrder());
    }
}
