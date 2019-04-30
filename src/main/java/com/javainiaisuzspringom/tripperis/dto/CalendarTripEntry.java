package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.CalendarEntryType;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class CalendarTripEntry implements CalendarEntry {

    private Integer tripId;
    private Timestamp start;
    private Timestamp end;
    private CalendarEntryType type;

    public CalendarTripEntry(Integer tripId, Timestamp startDate, Timestamp endDate) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
    }

    public CalendarTripEntry(Integer tripId, Timestamp startDate, Timestamp endDate, CalendarEntryType type) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
        this.type = type;
    }

    /**
     * This constructor is used in {@link AccountRepository#getTripDates(Account, Timestamp, Timestamp)}
     */
    public CalendarTripEntry(int tripId, Date startDate, Date endDate) {
        this.tripId = tripId;
        this.start = startDate != null ? Timestamp.from(startDate.toInstant()) : null;
        this.end = endDate != null ? Timestamp.from(endDate.toInstant()) : null;
    }
}
