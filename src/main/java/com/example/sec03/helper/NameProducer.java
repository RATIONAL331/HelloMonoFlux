package com.example.sec03.helper;

import com.example.util.Util;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class NameProducer implements Consumer<FluxSink<String>> {
    private FluxSink<String> fluxSink;

    @Override
    public void accept(FluxSink<String> stringFluxSink) {
        this.fluxSink = stringFluxSink;
    }

    public void produce() {
        String fullName = Util.faker().name().fullName();
        String threadName = Thread.currentThread().getName();
        this.fluxSink.next(fullName + ":" + threadName);
    }

    public void complete() {
        this.fluxSink.complete();
    }

    public void error(Throwable throwable) {
        this.fluxSink.error(throwable);
    }
}
