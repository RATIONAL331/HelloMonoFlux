package com.example.sec09.helper;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@ToString
public class RevenueReport {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final Map<String, Double> revenue;

    public RevenueReport(Map<String, Double> revenue) {
        this.revenue = revenue;
    }
}
