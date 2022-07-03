package com.example.sec02;

import com.example.sec02.assignment.StockPricePublisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

public class Lec10StockPriceAssignment {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StockPricePublisher.getPrice()
                           .subscribeWith(new Subscriber<Integer>() {
                               private Subscription subscription;

                               @Override
                               public void onSubscribe(Subscription subscription) {
                                   this.subscription = subscription;
                                   this.subscription.request(Long.MAX_VALUE);
                               }

                               @Override
                               public void onNext(Integer integer) {
                                   System.out.println(LocalDateTime.now() + ": Price => " + integer);
                                   if (integer > 110 || integer < 90) {
                                       this.subscription.cancel();
                                       latch.countDown();
                                   }
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   latch.countDown();
                               }

                               @Override
                               public void onComplete() {
                                   System.out.println("Completed");
                                   latch.countDown();
                               }
                           });
        latch.await();
    }
}
