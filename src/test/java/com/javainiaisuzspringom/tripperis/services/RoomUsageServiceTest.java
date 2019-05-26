package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.repositories.RoomUsageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoomUsageServiceTest {

    @Mock
    private RoomUsageRepository repository;

    @InjectMocks
    private RoomUsageService service;

    @Before
    public void setUp() {
        initMocks(this);
    }

    /**
     * Max size is 5, current reservations for that period are (each is with one person):
     * ==|====  |
     *   |======|=
     *   |==    |
     * Test should return true, if trying to get 2 reservations
     */
    @Test
    public void shouldBeAvailableForUsage() {
        RoomUsage usagePayload = mock(RoomUsage.class, RETURNS_DEEP_STUBS);
        Account reservingAccount = mock(Account.class);
        int roomCapacity = 5;
        Timestamp fromDate = Timestamp.valueOf(LocalDateTime.of(2012, 12, 12, 1, 1));
        Timestamp toDate = Timestamp.valueOf(LocalDateTime.of(2012, 12, 22, 1, 1));;

        RoomUsage reservation1 = getUsageWithOffsetAndPeople(fromDate, toDate, -1, -1, 1);
        RoomUsage reservation2 = getUsageWithOffsetAndPeople(fromDate, toDate, 0, 1, 1);
        RoomUsage reservation3 = getUsageWithOffsetAndPeople(fromDate, toDate, 0, -2, 1);


        when(usagePayload.getRoom().getMaxCapacity())
                .thenReturn(roomCapacity);
        when(usagePayload.getAccounts())
                .thenReturn(Arrays.asList(reservingAccount, reservingAccount));
        when(usagePayload.getApartmentUsage().getFrom())
                .thenReturn(fromDate);
        when(usagePayload.getApartmentUsage().getTo())
                .thenReturn(toDate);
        when(repository.findAllRoomUsagesBetweenDates(fromDate, toDate))
                .thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        boolean isAvailable = service.isAvailableForUsage(usagePayload);
        assertTrue(isAvailable);
    }

    /**
     * Max size is 3, current reservations for that period are (each is with one person):
     * ==|====  |
     *   |======|=
     *   |==    |
     * Test should return false, if trying to get 2 reservations
     */
    @Test
    public void shouldNotBeAvailableForUsage() {
        RoomUsage usagePayload = mock(RoomUsage.class, RETURNS_DEEP_STUBS);
        Account reservingAccount = mock(Account.class);
        int roomCapacity = 3;
        Timestamp fromDate = Timestamp.valueOf(LocalDateTime.of(2012, 12, 12, 1, 1));
        Timestamp toDate = Timestamp.valueOf(LocalDateTime.of(2012, 12, 22, 1, 1));;

        RoomUsage reservation1 = getUsageWithOffsetAndPeople(fromDate, toDate, -1, -1, 1);
        RoomUsage reservation2 = getUsageWithOffsetAndPeople(fromDate, toDate, 0, 1, 1);
        RoomUsage reservation3 = getUsageWithOffsetAndPeople(fromDate, toDate, 0, -2, 1);


        when(usagePayload.getRoom().getMaxCapacity())
                .thenReturn(roomCapacity);
        when(usagePayload.getAccounts())
                .thenReturn(Arrays.asList(reservingAccount, reservingAccount));
        when(usagePayload.getApartmentUsage().getFrom())
                .thenReturn(fromDate);
        when(usagePayload.getApartmentUsage().getTo())
                .thenReturn(toDate);
        when(repository.findAllRoomUsagesBetweenDates(fromDate, toDate))
                .thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        boolean isAvailable = service.isAvailableForUsage(usagePayload);
        assertFalse(isAvailable);
    }

    private RoomUsage getUsageWithOffsetAndPeople(Timestamp from, Timestamp to, int dayOffsetFrom, int dayOffsetTo, int peopleAmount) {
        RoomUsage takenFor = mock(RoomUsage.class, RETURNS_DEEP_STUBS);
        when(takenFor.getApartmentUsage().getFrom())
                .thenReturn(Timestamp.from(from.toInstant().plus(dayOffsetFrom, ChronoUnit.DAYS)));
        when(takenFor.getApartmentUsage().getTo())
                .thenReturn(Timestamp.from(to.toInstant().plus(dayOffsetTo, ChronoUnit.DAYS)));
        when(takenFor.getAccounts().size())
                .thenReturn(peopleAmount);
        return takenFor;
    }
}