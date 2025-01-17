package com.javainiaisuzspringom.tripperis.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.CalendarEntryType;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class CalendarTripEntry implements CalendarEntry {

    private Integer tripId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp start;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp end;

    private CalendarEntryType type;

    public CalendarTripEntry(Integer tripId, Timestamp startDate, Timestamp endDate) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
        this.type = CalendarEntryType.TRIP;
    }

    /**
     * This constructor is used in {@link AccountRepository#getTripDates(Account, Timestamp, Timestamp)}
     */
    public CalendarTripEntry(int tripId, Date startDate, Date endDate) {
        this.tripId = tripId;
        this.start = startDate != null ? Timestamp.from(startDate.toInstant()) : null;
        this.end = endDate != null ? Timestamp.from(endDate.toInstant()) : null;
        this.type = CalendarEntryType.TRIP;
    }
}
