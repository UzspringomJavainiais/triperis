package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
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
        Timestamp from = usage.getApartmentUsage().getFrom();
        Timestamp to = usage.getApartmentUsage().getTo();

        List<RoomUsage> usagesInPeriod = repository.findAllRoomUsagesBetweenDates(from, to);
        if(usagesInPeriod.isEmpty()) {
            return true;
        }
        Map<Duration, RoomUsage> durationToUsage = usagesInPeriod.stream()
                .collect(Collectors.toMap(x -> new Duration(x.getApartmentUsage().getFrom(), x.getApartmentUsage().getTo()), Function.identity()));
        new ArrayList<>(durationToUsage.keySet()).sort(Comparator.naturalOrder());
        return ;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Duration implements Comparable<Duration>{
        Timestamp from;
        Timestamp to;

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

        @Override
        public int compareTo(Duration d) {
            return this.from.compareTo(d.getFrom());
        }
    }
}
