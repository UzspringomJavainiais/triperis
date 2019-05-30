package com.javainiaisuzspringom.tripperis.domain.statistics;

import java.io.Serializable;

public class StatisticsResult implements Serializable {

    private String tripName;
    private Integer dateDiff;

    public String getTripName() {
        return tripName;
    }

    public StatisticsResult setTripName(String tripName) {
        this.tripName = tripName;
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
