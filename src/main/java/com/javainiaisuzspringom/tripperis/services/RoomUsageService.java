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
        ArrayList<Duration> durations = new ArrayList<>(durationToUsage.keySet());

        // Make these static?
        FromComparator fromComparator = new FromComparator();
        ToComparator toComparator = new ToComparator();

        durations.sort(fromComparator); // Durations sorted by starting date

        List<Duration> currentBucket = new ArrayList<>();
        List<Integer> capacityList = new ArrayList<>();

        Timestamp rootDate = durations.get(0).getFrom();


        Iterator<Duration> durationIterator = durations.iterator();

        while(durationIterator.hasNext()) {
            List<Duration> durationsThatStartOnDate = getDurationsThatStartOnDate(durations, rootDate);
            currentBucket.addAll(durationsThatStartOnDate);
            currentBucket.sort(toComparator); // durations in bucket sorted by ending date

            capacityList.add(maxCapacity - currentBucket.stream().mapToInt(Duration::getReservations).sum()); // Max capacity - sum of all reservations at that moment
            if (durationIterator.hasNext() && !currentBucket.get(0).to.before(durationIterator.next().to)) { // min(to) >= next().to
                rootDate = durationIterator.next().to;
            }
            else {
                Timestamp to = currentBucket.get(0).to;
                currentBucket.removeIf(x -> x.to == to);
                rootDate = to;
            }
        }

        // If there exists a moment in given period, where available capacity is less than needed, then return false
        return capacityList.stream()
                .allMatch(capacityAtMoment -> capacityAtMoment >= peopleCount);
    }

    private List<Duration> getDurationsThatStartOnDate(ArrayList<Duration> durations, Timestamp rootDate) {
        return durations.stream().filter(x -> x.from.equals(rootDate)).collect(Collectors.toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Duration {
        Timestamp from;
        Timestamp to;
        int reservations;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Duration duration = (Duration) o;
            return from.equals(duration.from) &&
                    to.equals(duration.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    private static class FromComparator implements Comparator<Duration> {

        @Override
        public int compare(Duration o1, Duration o2) {
            return o1.from.compareTo(o2.from);
        }
    }

    private static class ToComparator implements Comparator<Duration> {

        @Override
        public int compare(Duration o1, Duration o2) {
            return o1.to.compareTo(o2.to);
        }
    }


    @Getter
    @Setter
    @AllArgsConstructor
    private static class Capacity {
        Timestamp from;
        Timestamp to;
        int count;
    }
}
