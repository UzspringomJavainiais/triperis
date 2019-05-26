package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Room;
import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.dto.ReservationInfo;
import com.javainiaisuzspringom.tripperis.dto.ReservationInfo.FromComparator;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomUsageRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomUsageService implements BasicDtoToEntityService<RoomUsage, RoomUsageDTO, Integer> {

    @Autowired
    @Getter
    private RoomUsageRepository repository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final FromComparator fromComparator = new FromComparator();

    @Override
    public RoomUsage convertToEntity(RoomUsageDTO dto) {
        RoomUsage entity = new RoomUsage();

        if(dto.getRoomId() != null) {
            entity.setRoom(roomRepository.getOne(dto.getRoomId()));
        }
        if(dto.getAccountIds() != null) {
            entity.setAccounts(dto.getAccountIds().stream().map(id -> accountRepository.getOne(id)).collect(Collectors.toList()));
        }

        return entity;
    }

    public boolean isAvailableForUsage(RoomUsage usage) {
        Integer maxCapacity = usage.getRoom().getMaxCapacity();
        int peopleCount = usage.getAccounts().size();
        if(peopleCount > maxCapacity) {
            return false;
        }
        Timestamp usageFrom = usage.getApartmentUsage().getFrom();
        Timestamp usageTo = usage.getApartmentUsage().getTo();
        Room room = usage.getRoom();

        List<ReservationInfo> reservedList = getReservedCapacities(room, usageFrom, usageTo);

        // If there exists a moment in given period, where available capacity is less than needed, then return false
        return reservedList.stream()
                .allMatch(reservationsAtMoment -> (maxCapacity - reservationsAtMoment.getReservations()) >= peopleCount);
    }

    public List<ReservationInfo> getReservedCapacities(Room room, Timestamp usageFrom, Timestamp usageTo) {
        List<RoomUsage> usagesInPeriod = repository.findAllRoomUsagesBetweenDates(room, usageFrom, usageTo);
        List<ReservationInfo> durations = usagesInPeriod.stream()
                .map(x -> new ReservationInfo(x.getApartmentUsage().getFrom(), x.getApartmentUsage().getTo(), x.getAccounts().size()))
                .collect(Collectors.toList());
        return getReservedList(durations);
    }

    protected List<ReservationInfo> getReservedList(List<ReservationInfo> durations) {

        durations.sort(fromComparator); // Durations sorted by starting date

        Deque<ReservationInfo> stack = new ArrayDeque<>(durations);
        List<ReservationInfo> currentBucket = new ArrayList<>();
        List<ReservationInfo> capacityList = new ArrayList<>();

        Timestamp rootDate = durations.get(0).getFrom();

        while(!(currentBucket.isEmpty() && stack.isEmpty())) {
            // 1) Add all durations with lowest start date to bucket
            while(stack.peek() != null && stack.peek().getFrom().equals(rootDate)) {
                currentBucket.add(stack.pop());
            }
            // 2) Count capacity for bucket
            int reservations = currentBucket.stream().mapToInt(ReservationInfo::getReservations).sum();
            // 3) Get next root date
            Timestamp minToDate = currentBucket.stream().map(ReservationInfo::getTo).min(Timestamp::compareTo).get();
            Timestamp oldRootDate = rootDate;
            if(!stack.isEmpty() && minToDate.after(stack.peek().getFrom())) {
                rootDate = stack.peek().getFrom();
            }
            else {
                // Remove all expired dates
                currentBucket.removeIf(x -> x.getTo() == minToDate);
                rootDate = minToDate;
            }

            ReservationInfo duration = new ReservationInfo(oldRootDate, rootDate, reservations);
            capacityList.add(duration);
        }
        return capacityList;
    }
}
