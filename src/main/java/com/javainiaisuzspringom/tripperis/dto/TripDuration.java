package com.javainiaisuzspringom.tripperis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class TripDuration {
    private Integer tripId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp start;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp end;

    public TripDuration(Integer tripId, Timestamp startDate, Timestamp endDate) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
    }

    /**
     * This constructor is used in {@link TripRepository#getDuration(Trip)}}
     */
    public TripDuration(int tripId, Date startDate, Date endDate) {
        this.tripId = tripId;
        this.start = startDate != null ? Timestamp.from(startDate.toInstant()) : null;
        this.end = endDate != null ? Timestamp.from(endDate.toInstant()) : null;
    }
}
