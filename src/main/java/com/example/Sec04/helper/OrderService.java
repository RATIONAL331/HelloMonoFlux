package com.example.Sec04.helper;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private static final Map<Integer, List<PurchaseOrder>> db = new HashMap<>();

    static {
        db.put(1, Arrays.asList(new PurchaseOrder(1), new PurchaseOrder(1), new PurchaseOrder(1)));
        db.put(2, Arrays.asList(new PurchaseOrder(2), new PurchaseOrder(2)));
        db.put(3, Arrays.asList(new PurchaseOrder(3)));
    }

    public static Flux<PurchaseOrder> getOrder(int userId) {
        return Flux.<PurchaseOrder>create(sink -> {
                       db.get(userId).forEach(sink::next);
                       sink.complete();
                   })
                   .delayElements(Duration.ofMillis(100));
    }
}
