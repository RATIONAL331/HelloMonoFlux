package com.example.sec04;

import com.example.sec04.helper.Person;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Lec11SwitchOnFirst {
    public static void main(String[] args) {
        // signal [onNext, onError, onComplete, onSubscribe]
        getPersons().switchOnFirst(((signal, personFlux) -> {
                        System.out.println("inside switch-on-first");
                        return signal.isOnNext() && signal.get().getAge() > 5 ? personFlux : applyFilterMap().apply(personFlux);
                    }))
                    .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }

    /**
     * inside switch-on-first
     * Received: Person(name=Nancey, age=6)
     * Received: Person(name=Shanon, age=3) <- notice
     * Received: Person(name=Kayce, age=5)
     * Received: Person(name=Fritz, age=10)
     * Received: Person(name=Annalee, age=9)
     * Received: Person(name=Shara, age=8)
     * Received: Person(name=Lindsay, age=7)
     * Received: Person(name=Chuck, age=4)
     * Received: Person(name=Hong, age=1)
     * Received: Person(name=Rosalia, age=2)
     * Completed
     */

    /**
     * inside switch-on-first
     * Not allowing : Person(name=Hollis, age=3)
     * Not allowing : Person(name=Amiee, age=3)
     * Received: Person(name=ELODIA, age=7)
     * Not allowing : Person(name=Arnoldo, age=1)
     * Not allowing : Person(name=Austin, age=3)
     * Not allowing : Person(name=Glenn, age=1)
     * Not allowing : Person(name=Orville, age=5)
     * Not allowing : Person(name=Modesta, age=2)
     * Received: Person(name=INGEBORG, age=7)
     * Received: Person(name=ELMO, age=9)
     * Completed
     */

    public static Flux<Person> getPersons() {
        return Flux.range(1, 10)
                   .map(i -> new Person());
    }

    public static Function<Flux<Person>, Flux<Person>> applyFilterMap() {
        return flux -> flux.filter(person -> person.getAge() > 5)
                           .doOnNext(person -> person.setName(person.getName().toUpperCase()))
                           .doOnDiscard(Person.class, person -> System.out.println("Not allowing : " + person));
    }
}
