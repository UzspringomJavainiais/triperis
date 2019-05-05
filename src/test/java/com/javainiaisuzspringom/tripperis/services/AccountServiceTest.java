package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
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

public class AccountServiceTest {

    @Mock
    private AccountRepository mockRepository;

    @Mock
    private Account mockAccount;

    @InjectMocks
    private AccountService accountService;

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

        Object[] objects = {tripId, tripStart, tripEnd};
        List<Object[]> listOfObjects = new ArrayList<>();
        listOfObjects.add(objects);

        when(mockRepository.getTripDates(eq(mockAccount), any(), any()))
                .thenReturn(listOfObjects);

        List<TripDuration> tripDurationsInPeriod = accountService.getTripDurationsInPeriod(mockAccount, startDate, endDate);
        assertThat(tripDurationsInPeriod.size(), is(1));

        TripDuration tripDuration = tripDurationsInPeriod.get(0);
        assertThat(tripDuration.getTripId(), is(tripId));
        assertThat(tripDuration.getStart(), is(tripStart));
        assertThat(tripDuration.getEnd(), is(tripEnd));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenQueryResultsIncorrect() {
        Instant startInstant = Instant.parse("2019-04-30T10:15:30.00Z");
        Instant endInstant = Instant.parse("2019-05-03T10:15:30.00Z");
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        Object[] objects = {1, 2};
        List<Object[]> listOfObjects = new ArrayList<>();
        listOfObjects.add(objects);

        when(mockRepository.getTripDates(eq(mockAccount), any(), any()))
                .thenReturn(listOfObjects);

        accountService.getTripDurationsInPeriod(mockAccount, startDate, endDate);
    }
}