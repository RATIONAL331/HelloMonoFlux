package com.example.sec08;

import com.example.sec08.helper.AsianaFlights;
import com.example.sec08.helper.JejuFlights;
import com.example.sec08.helper.KALFlights;
import com.example.util.DefaultSubscriber;
import com.example.util.Util;
import reactor.core.publisher.Flux;

public class Lec03Merge {
    public static void main(String[] args) {
        Flux<String> kal = KALFlights.getFlights();
        Flux<String> asiana = AsianaFlights.getFlights();
        Flux<String> jeju = JejuFlights.getFlights();

        Flux<String> merge = Flux.merge(kal, asiana, jeju);

        merge.subscribe(new DefaultSubscriber("subscriber1"));

        /**
         * subscriber1; Received: KAL: 895
         * subscriber1; Received: JEJU: 180
         * subscriber1; Received: JEJU: 506
         * subscriber1; Received: KAL: 812
         * subscriber1; Received: JEJU: 642
         * subscriber1; Received: KAL: 205
         * subscriber1; Received: AAA: 793
         * subscriber1; Received: JEJU: 803
         * subscriber1; Received: JEJU: 809
         * subscriber1; Received: KAL: 497
         * subscriber1; Received: AAA: 234
         * subscriber1; Received: AAA: 749
         * subscriber1; Completed
         */
        Util.sleepSeconds(10);
        System.out.println("===============================");

        Flux<String> concat = Flux.concat(kal, asiana, jeju);
        concat.subscribe(new DefaultSubscriber("subscriber2"));
        Util.sleepSeconds(10);

        /**
         * subscriber2; Received: KAL: 555
         * subscriber2; Received: KAL: 540
         * subscriber2; Received: KAL: 405
         * subscriber2; Received: AAA: 893 <- notice when KAL finished then AAA started
         * subscriber2; Received: JEJU: 998
         * subscriber2; Completed
         */
    }
}
