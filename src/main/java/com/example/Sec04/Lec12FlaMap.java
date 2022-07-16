package com.example.Sec04;

import com.example.Sec04.helper.OrderService;
import com.example.Sec04.helper.User;
import com.example.Sec04.helper.UserService;
import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec12FlaMap {
    public static void main(String[] args) {
        Flux<User> users = UserService.getUsers();
        users.map(user -> OrderService.getOrder(user.getUserId())) // return Flux
             .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Util.sleepSeconds(1);
        /**
         * Received: FluxCreate
         * Received: FluxCreate
         * Received: FluxCreate
         * Completed
         */
        System.out.println("===========================================================");

        Flux<User> userFlux = UserService.getUsers();
        userFlux.flatMap(user -> OrderService.getOrder(user.getUserId()))
                .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        Util.sleepSeconds(1);
        /**
         * Received: PurchaseOrder(item=Aerodynamic Iron Coat, price=24.18, userId=3) // not ordered
         * Received: PurchaseOrder(item=Aerodynamic Aluminum Keyboard, price=94.15, userId=1)
         * Received: PurchaseOrder(item=Aerodynamic Wool Hat, price=57.20, userId=2)
         * Received: PurchaseOrder(item=Small Wooden Watch, price=21.35, userId=2)
         * Received: PurchaseOrder(item=Sleek Rubber Table, price=53.85, userId=1)
         * Received: PurchaseOrder(item=Incredible Aluminum Bottle, price=32.99, userId=1)
         * Completed
         */
        System.out.println("===========================================================");

        Flux<User> concatUsers = UserService.getUsers();
        concatUsers.concatMap(user -> OrderService.getOrder(user.getUserId())) // when first publisher complete signal then second publisher emit
                   .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
        /**
         * Received: PurchaseOrder(item=Aerodynamic Aluminum Keyboard, price=94.15, userId=1) // ordered
         * Received: PurchaseOrder(item=Sleek Rubber Table, price=53.85, userId=1)
         * Received: PurchaseOrder(item=Incredible Aluminum Bottle, price=32.99, userId=1)
         * Received: PurchaseOrder(item=Aerodynamic Wool Hat, price=57.20, userId=2)
         * Received: PurchaseOrder(item=Small Wooden Watch, price=21.35, userId=2)
         * Received: PurchaseOrder(item=Aerodynamic Iron Coat, price=24.18, userId=3)
         * Completed
         */
        Util.sleepSeconds(1);
    }
}
