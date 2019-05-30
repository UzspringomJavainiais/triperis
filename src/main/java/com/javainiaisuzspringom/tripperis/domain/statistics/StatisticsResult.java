package com.javainiaisuzspringom.tripperis.domain.statistics;

import java.io.Serializable;

public class StatisticsResult implements Serializable {

    private String name;
    private Integer dateDiff;

    public String getName() {
        return name;
    }

    public StatisticsResult setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDateDiff() {
        return dateDiff;
    }

    public StatisticsResult setDateDiff(Integer dateDiff) {
        this.dateDiff = dateDiff;
        return this;
    }
}
