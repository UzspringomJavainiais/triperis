package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RoomUsageRepository extends JpaRepository<RoomUsage, Integer> {

    @Query("SELECT ru FROM RoomUsage ru " +
                "LEFT JOIN ru.apartmentUsage au " +
            "WHERE ((au.from >= :from AND au.to <= :to) " +
                "OR (au.to > :from AND au.from < :to))")
    public List<RoomUsage> findAllRoomUsagesBetweenDates(Timestamp from, Timestamp to);
}