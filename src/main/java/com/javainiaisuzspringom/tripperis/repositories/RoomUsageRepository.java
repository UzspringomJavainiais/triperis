package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomUsageRepository extends JpaRepository<RoomUsage, Integer> {

    @Query("SELECT ru FROM RoomUsage ru " +
                "LEFT JOIN ru.apartmentUsage au " +
            "WHERE ((au.from >= :periodStart AND au.to <= :periodEnd) " +
                "OR (au.to > :periodStart AND au.from < :periodEnd)) " +
            "AND ru = :usage")
    public List<Account> findAllUsersUsingRoomInPeriod(RoomUsage usage);
}