package com.example.sec09.helper;

import com.example.util.Util;
import com.github.javafaker.Book;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BookOrder {
    private final String title;
    private final String author;
    private final String category;
    private final double price;

    public BookOrder() {
        Book book = Util.faker().book();
        this.title = book.title();
        this.author = book.author();
        this.category = book.genre();
        this.price = Double.parseDouble(Util.faker().commerce().price());
    }
}
