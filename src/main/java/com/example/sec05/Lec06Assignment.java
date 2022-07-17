package com.example.sec05;

import com.example.sec05.assignment.InventoryService;
import com.example.sec05.assignment.OrderService;
import com.example.sec05.assignment.RevenueService;
import com.example.util.DefaultSubscriber;
import com.example.util.Util;

public class Lec06Assignment {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        RevenueService revenueService = new RevenueService();
        InventoryService inventoryService = new InventoryService();

        orderService.orderStream()
                    .subscribe(revenueService.subscribeOrderStream());
        orderService.orderStream()
                    .subscribe(inventoryService.subscribeOrderStream());

        // subscribe finish

        inventoryService.inventoryStream()
                        .subscribe(DefaultSubscriber.subscriber("inventory"));
        revenueService.revenueStream()
                      .subscribe(DefaultSubscriber.subscriber("revenue"));
        /**
         * revenue; Received: {Kids=95.62, Automotive=68.2}
         * inventory; Received: {Kids=109, Automotive=110}
         * inventory; Received: {Kids=117, Automotive=127}
         * revenue; Received: {Kids=125.2, Automotive=176.76999999999998}
         * inventory; Received: {Kids=117, Automotive=127}
         * revenue; Received: {Kids=125.2, Automotive=176.76999999999998}
         * inventory; Received: {Kids=117, Automotive=127}
         * revenue; Received: {Kids=125.2, Automotive=176.76999999999998}
         * inventory; Received: {Kids=117, Automotive=127}
         * revenue; Received: {Kids=125.2, Automotive=176.76999999999998}
         */

        Util.sleepSeconds(10);
    }
}
