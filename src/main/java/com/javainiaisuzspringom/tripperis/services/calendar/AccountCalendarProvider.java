package com.javainiaisuzspringom.tripperis.services.calendar;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;

import java.util.Date;
import java.util.List;

/**
 * Provides info about {@link Account} status in some period of time
 */
public interface AccountCalendarProvider {
    List<CalendarEntry> getAccountCalendar(Account account, Date periodStart, Date periodEnd);
}
