package com.example.sec11.assignment;

import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Getter
public class SlackRoom {
    private final String name;
    private final Sinks.Many<SlackMessage> sink;
    private final Flux<SlackMessage> flux;

    public SlackRoom(String name) {
        this.name = name;
        this.sink = Sinks.many().replay().all();
        this.flux = sink.asFlux();
    }

    public void joinRoom(SlackMember slackMember) {
        System.out.println(slackMember.getName() + " --- joined --- " + name);
        this.subscribe(slackMember);
        slackMember.setMessageConsumer(msg -> this.postMessage(msg, slackMember));
    }

    private void subscribe(SlackMember slackMember) {
        this.flux//.filter(slackMessage -> !slackMessage.getSender().equals(slackMember.getName())) // if same sender, ignore
                 .doOnNext(slackMessage -> slackMessage.setReceiver(slackMember.getName()))
                 .subscribe(slackMessage -> slackMember.receive(slackMessage.toString()));
    }

    private void postMessage(String msg, SlackMember slackMember) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setSender(slackMember.getName());
        slackMessage.setMessage(msg);
        this.sink.tryEmitNext(slackMessage);
    }
}
