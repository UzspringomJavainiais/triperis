package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TripServiceTest {

    @Mock
    private TripRepository mockRepository;

    @Mock
    private Trip mockTrip;

    @InjectMocks
    private TripService accountService;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void shouldParseRepoResultsCorrectly() {

        int tripId = 30;
        Timestamp tripStart = Timestamp.valueOf("2019-04-31 10:10:00");
        Timestamp tripEnd = Timestamp.valueOf("2019-05-01 10:10:00");
        List<Object[]> objects = new ArrayList<>();

        objects.add(new Object[]{tripStart, tripEnd});

        when(mockTrip.getId())
                .thenReturn(tripId);
        when(mockRepository.getDuration(mockTrip))
                .thenReturn(objects);

        Optional<TripDuration> maybeTripDuration = accountService.getTripDuration(mockTrip);
        assertThat(maybeTripDuration.isPresent(), is(true));
        TripDuration tripDuration = maybeTripDuration.get();
        assertThat(tripDuration.getTripId(), is(tripId));
        assertThat(tripDuration.getStart(), is(tripStart));
        assertThat(tripDuration.getEnd(), is(tripEnd));
    }

    @Test
    public void shouldReturnEmptyWhenNoResult() {

        List<Object[]> objects = Collections.emptyList();

        when(mockRepository.getDuration(mockTrip))
                .thenReturn(objects);

        Optional<TripDuration> maybeTripDuration = accountService.getTripDuration(mockTrip);
        assertThat(maybeTripDuration.isPresent(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenQueryResultsIncorrectLessThanTwo() {
        Object[] objects = {1};
        List<Object[]> listOfObjects = new ArrayList<>();
        listOfObjects.add(objects);

        when(mockRepository.getDuration(mockTrip))
                .thenReturn(listOfObjects);

        accountService.getTripDuration(mockTrip);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenQueryResultsIncorrectMoreThanTwo() {
        Object[] objects = {1, 1, 1};
        List<Object[]> listOfObjects = new ArrayList<>();
        listOfObjects.add(objects);

        when(mockRepository.getDuration(mockTrip))
                .thenReturn(listOfObjects);

        accountService.getTripDuration(mockTrip);
    }
}