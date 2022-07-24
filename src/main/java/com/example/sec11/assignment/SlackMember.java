package com.example.sec11.assignment;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class SlackMember {
    private final String name;
    private Consumer<String> messageConsumer;

    public SlackMember(String name) {
        this.name = name;
    }

    void receive(String message) {
        System.out.println(name + " got message: " + message);
    }

    public void says(String message) {
        this.messageConsumer.accept(message);
    }
}
