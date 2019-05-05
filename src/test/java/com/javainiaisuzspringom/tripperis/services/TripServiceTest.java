package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TripServiceTest {

    @Mock
    private TripRepository mockRepository;

    @Mock
    private TripDTO mockTrip;

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
        List<TripDuration> objects = new ArrayList<>();

        objects.add(new TripDuration(tripId, tripStart, tripEnd));

        when(mockTrip.getId())
                .thenReturn(tripId);
        when(mockRepository.getDuration(any()))
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

        List<TripDuration> objects = Collections.emptyList();

        Integer tripId = 455;
        when(mockTrip.getId())
                .thenReturn(tripId);

        when(mockRepository.getDuration(any()))
                .thenReturn(objects);

        Optional<TripDuration> maybeTripDuration = accountService.getTripDuration(mockTrip);
        assertThat(maybeTripDuration.isPresent(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenMultipleQueryResults() {
        Integer tripId = 414;
        List<TripDuration> listOfObjects = new ArrayList<>();
        TripDuration duration = new TripDuration(tripId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        listOfObjects.add(duration);
        listOfObjects.add(duration);

        when(mockTrip.getId())
                .thenReturn(tripId);

        when(mockRepository.getDuration(any()))
                .thenReturn(listOfObjects);

        accountService.getTripDuration(mockTrip);
    }
}