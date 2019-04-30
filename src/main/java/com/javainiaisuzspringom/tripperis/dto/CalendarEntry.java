package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import lombok.Data;

import java.util.Date;
import java.sql.Timestamp;

@Data
public class CalendarEntry {
    private Integer tripId;
    private Timestamp start;
    private Timestamp end;
//    private CalendarEntryType type;

    public CalendarEntry(Integer tripId, Timestamp startDate, Timestamp endDate) {
        this.tripId = tripId;
        this.start = startDate;
        this.end = endDate;
//        this.type = type;
    }

    /**
     * This constructor is used in {@link AccountRepository#getTripDates(Account, Timestamp, Timestamp)}
     */
    public CalendarEntry(int tripId, Date startDate, Date endDate) {
        this.tripId = tripId;
        this.start = Timestamp.from(startDate.toInstant());
        this.end = Timestamp.from(endDate.toInstant());
//        this.type = type;
    }
}
