package com.example.sec12;

import com.example.sec12.helper.BookService;
import com.example.sec12.helper.UserService;
import com.example.util.DefaultSubscriber;
import reactor.util.context.Context;

public class Lec02Demo {
    public static void main(String[] args) {
        BookService.getBook()
                   .subscribe(new DefaultSubscriber("subscriber"));

        System.out.println("===============================");
        /**
         * subscriber; Error: java.lang.RuntimeException: not allowed
         */

        BookService.getBook()
                   .repeat(2)
                   .contextWrite(UserService.userCategoryContext())
                   .contextWrite(Context.of("user", "sam"))
                   .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: The Widening Gyre
         * subscriber; Received: The Glory and the Dream
         * subscriber; Error: java.lang.RuntimeException: not allowed
         */
        System.out.println("===============================");

        BookService.getBook()
                   .repeat(2)
                   .contextWrite(UserService.userCategoryContext())
                   .contextWrite(Context.of("user", "mike"))
                   .subscribe(new DefaultSubscriber("subscriber"));

        /**
         * subscriber; Received: Tender Is the Night
         * subscriber; Received: Of Mice and Men
         * subscriber; Received: The Golden Bowl
         * subscriber; Completed
         */
    }
}
