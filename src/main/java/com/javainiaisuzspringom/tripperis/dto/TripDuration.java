package com.javainiaisuzspringom.tripperis.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TripDuration {
    private Integer tripId;
    private Timestamp start;
    private Timestamp end;

    public TripDuration(Integer tripId, Timestamp startDate, Timestamp endDate) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
    }
}
