package com.example.sec04;

import com.example.sec04.helper.Person;
import com.example.util.Util;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Lec10Transform {
    public static void main(String[] args) {
        getPersons().transform(applyFilterMap()) // Flux to another Flux (reusable)
                    .subscribe(Util.onNext(), Util.onError(), Util.onComplete());
    }

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
