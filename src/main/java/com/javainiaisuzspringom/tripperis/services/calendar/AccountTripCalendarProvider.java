package com.javainiaisuzspringom.tripperis.services.calendar;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class AccountTripCalendarProvider implements AccountCalendarProvider {

    @Autowired
    private AccountRepository accountRepository;

    public List<CalendarEntry> getAccountCalendar(Account account, Date periodStart, Date periodEnd) {
        Timestamp startTimestamp = Timestamp.from(periodStart.toInstant());
        Timestamp endTimestamp = Timestamp.from(periodEnd.toInstant());

        return accountRepository.getTripDates(account, startTimestamp, endTimestamp);
    }
}
