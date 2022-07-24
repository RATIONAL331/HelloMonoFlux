package com.example.sec11.assignment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlackMessage {
    private static final String FORMAT = "[%s -> %s]: [%s]";
    private String sender;
    private String receiver;
    private String message;

    @Override
    public String toString() {
        return String.format(FORMAT, sender, receiver, message);
    }
}
