package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.CalendarTripEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.AccountTripCalendarProvider;
import com.javainiaisuzspringom.tripperis.services.calendar.CalendarEntryType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountTripCalendarProviderTest {

    @Mock
    private AccountRepository mockRepository;

    @Mock
    private AccountDTO mockAccount;

    @InjectMocks
    private AccountTripCalendarProvider calendarProvider;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void shouldParseRepoResultsCorrectly() {
        Instant startInstant = Instant.parse("2019-04-30T10:15:30.00Z");
        Instant endInstant = Instant.parse("2019-05-03T10:15:30.00Z");
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        int tripId = 666;
        Timestamp tripStart = Timestamp.valueOf("2019-04-31 10:10:00");
        Timestamp tripEnd = Timestamp.valueOf("2019-05-01 10:10:00");

        List<CalendarTripEntry> listOfObjects = new ArrayList<>();
        listOfObjects.add(new CalendarTripEntry(tripId, tripStart, tripEnd));


        Integer accountId = 120;
        when(mockAccount.getId())
                .thenReturn(accountId);
        when(mockRepository.getTripDates(eq(accountId), any(), any()))
                .thenReturn(listOfObjects);

        List<CalendarEntry> tripDurationsInPeriod = calendarProvider.getAccountCalendar(mockAccount, startDate, endDate);
        assertThat(tripDurationsInPeriod.size(), is(1));

        CalendarTripEntry tripDuration = (CalendarTripEntry) tripDurationsInPeriod.get(0);
        assertThat(tripDuration.getTripId(), is(tripId));
        assertThat(tripDuration.getStart(), is(tripStart));
        assertThat(tripDuration.getEnd(), is(tripEnd));
        assertThat(tripDuration.getType(), is(CalendarEntryType.TRIP));
    }
}