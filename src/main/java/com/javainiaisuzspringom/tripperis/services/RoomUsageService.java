package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomUsageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
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

        List<RoomUsage> usagesInPeriod = repository.findAllRoomUsagesBetweenDates(usageFrom, usageTo);
        if(usagesInPeriod.isEmpty()) {
            return true;
        }
        Map<Duration, RoomUsage> durationToUsage = usagesInPeriod.stream()
                .collect(Collectors.toMap(x -> new Duration(x.getApartmentUsage().getFrom(), x.getApartmentUsage().getTo(), x.getAccounts().size()), Function.identity()));
        LinkedList<Duration> durations = new LinkedList<>(durationToUsage.keySet());
        List<Integer> capacityList = getCapacityList(maxCapacity, durations);

        // If there exists a moment in given period, where available capacity is less than needed, then return false
        return capacityList.stream()
                .allMatch(capacityAtMoment -> capacityAtMoment >= peopleCount);
    }

    protected List<Integer> getCapacityList(Integer maxCapacity, List<Duration> durations) {

        durations.sort(fromComparator); // Durations sorted by starting date

        Deque<Duration> stack = new ArrayDeque<>(durations);
        List<Duration> currentBucket = new ArrayList<>();
        List<Integer> capacityList = new ArrayList<>();

        Timestamp rootDate = durations.get(0).getFrom();

        while(!(currentBucket.isEmpty() && stack.isEmpty())) {
            // 1) Add all durations with lowest start date to bucket
            while(stack.peek() != null && stack.peek().from.equals(rootDate)) {
                currentBucket.add(stack.pop());
            }
            // 2) Count capacity for bucket
            capacityList.add(maxCapacity - currentBucket.stream().mapToInt(Duration::getReservations).sum());
            // 3) Get next root date
            Timestamp minToDate = currentBucket.stream().map(Duration::getTo).min(Timestamp::compareTo).get();
            if(!stack.isEmpty() && minToDate.after(stack.peek().from)) {
                rootDate = stack.peek().from;
            }
            else {
                // Remove all expired dates
                currentBucket.removeIf(x -> x.to == minToDate);
                rootDate = minToDate;
            }
        }
        return capacityList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class Duration {
        Timestamp from;
        Timestamp to;
        int reservations;
    }

    private static class FromComparator implements Comparator<Duration> {

        @Override
        public int compare(Duration o1, Duration o2) {
            return o1.from.compareTo(o2.from);
        }
    }
}
