package com.example.Sec04.helper;

import com.example.util.Util;
import lombok.Data;

@Data
public class Person {
    private String name;
    private int age;

    public Person() {
        this.name = Util.faker().name().firstName();
        this.age = Util.faker().random().nextInt(1, 10);
    }
}
