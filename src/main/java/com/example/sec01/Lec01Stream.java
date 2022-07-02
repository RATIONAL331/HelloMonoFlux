package com.example.sec01;

import java.util.stream.Stream;

public class Lec01Stream {
    public static void main(String[] args) {
        System.out.println("begin");
        Stream<Integer> integerStream = Stream.of(1)
                                              .map(i -> {
                                                  try {
                                                      Thread.sleep(1000);
                                                  } catch (InterruptedException e) {
                                                      throw new RuntimeException(e);
                                                  }
                                                  return i * 2;
                                              });

//        System.out.println(integerStream);
        integerStream.forEach(System.out::println);
    }
}
