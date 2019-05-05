package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

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

    /**
     * This constructor is used in {@link TripRepository#getDuration(Integer)}
     */
    public TripDuration(int tripId, Date startDate, Date endDate) {
        this.tripId = tripId;
        this.start = startDate != null ? Timestamp.from(startDate.toInstant()) : null;
        this.end = endDate != null ? Timestamp.from(endDate.toInstant()) : null;
    }
}
