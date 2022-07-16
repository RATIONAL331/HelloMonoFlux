package com.example.Sec04.helper;

import com.example.util.Util;
import lombok.Data;

@Data
public class User {
    private int userId;
    private String name;

    public User(int userId) {
        this.userId = userId;
        this.name = Util.faker().name().firstName();
    }

}
