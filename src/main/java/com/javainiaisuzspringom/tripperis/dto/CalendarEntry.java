package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.services.calendar.CalendarEntryType;

import java.sql.Timestamp;

public interface CalendarEntry {
    Timestamp getStart();
    Timestamp getEnd();
    CalendarEntryType getType();
}
