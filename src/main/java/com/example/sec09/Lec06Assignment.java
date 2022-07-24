package com.example.sec09;

import com.example.sec09.assignment.OrderProcessor;
import com.example.sec09.assignment.OrderService;
import com.example.sec09.assignment.PurchaseOrder;
import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Lec06Assignment {
    public static void main(String[] args) {
        Map<String, Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>>> map = Map.of(
                "Kids", OrderProcessor.kidsProcessing(),
                "Automotive", OrderProcessor.automotiveProcessing()
        );

        Set<String> key = map.keySet();

        OrderService.orderStream()
                    .filter(purchaseOrder -> key.contains(purchaseOrder.getCategory()))
                    .groupBy(PurchaseOrder::getCategory) // group 2 key
                    .flatMap(group -> map.get(group.key()).apply(group)) // mapping group to function
                    .subscribe(new DefaultSubscriber("grouping"));

        Util.sleepSeconds(10);

        /**
         * grouping; Received: PurchaseOrder(item={{ Lightweight Iron Bag }}, price=87.989, category=Automotive)
         * grouping; Received: PurchaseOrder(item={{ Practical Steel Clock }}, price=42.125, category=Kids)
         * grouping; Received: PurchaseOrder(item=FREE - Fantastic Silk Knife, price=0.0, category=Kids)
         * grouping; Received: PurchaseOrder(item={{ Ergonomic Bronze Watch }}, price=7.245, category=Kids)
         * grouping; Received: PurchaseOrder(item=FREE - Aerodynamic Steel Knife, price=0.0, category=Kids)
         * grouping; Received: PurchaseOrder(item={{ Practical Marble Wallet }}, price=21.375, category=Kids)
         * grouping; Received: PurchaseOrder(item=FREE - Enormous Granite Shirt, price=0.0, category=Kids)
         */
    }
}
