package com.javainiaisuzspringom.tripperis.services.calendar;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarTripEntry;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gets {@link Account} taken status from its related {@link Trip}s
 */
public class AccountTripCalendarProvider implements AccountCalendarProvider {

    @Autowired
    private AccountRepository accountRepository;

    public List<CalendarEntry> getAccountCalendar(Account account, Date periodStart, Date periodEnd) {
        Timestamp startTimestamp = Timestamp.from(periodStart.toInstant());
        Timestamp endTimestamp = Timestamp.from(periodEnd.toInstant());

        List<CalendarTripEntry> tripDates = accountRepository.getTripDates(account, startTimestamp, endTimestamp);
        return tripDates.stream()
                .map(dates -> (CalendarEntry) dates)
                .collect(Collectors.toList());
    }

    public List<CalendarEntry> getAccountCalendar(Account account) {
        return account.getTrips().stream()
                .map(t -> new CalendarTripEntry(t.getId(), t.getDateFrom(), t.getDateTo()))
                .map(dates -> (CalendarEntry) dates)
                .collect(Collectors.toList());
    }
}
