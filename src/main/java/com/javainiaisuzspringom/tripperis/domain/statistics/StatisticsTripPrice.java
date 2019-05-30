package com.javainiaisuzspringom.tripperis.domain.statistics;

import java.math.BigDecimal;

public class StatisticsTripPrice {
    private String name;
    private BigDecimal sum;

    public String getName() {
        return name;
    }

    public StatisticsTripPrice setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public StatisticsTripPrice setSum(BigDecimal sum) {
        this.sum = sum;
        return this;
    }
}
