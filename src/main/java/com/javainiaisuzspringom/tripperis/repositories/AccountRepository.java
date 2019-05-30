package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarTripEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Query returns trip id and its period, where the trips are in the given period.
     * For example a trip might be from 1 and 5, and we request dates 2 and 4, so we'll get the given trip as a result
     * @param account account for which trips will be retrieved
     * @param periodStart period from which to check
     * @param periodEnd period until which to check
     * @return an array, where the first element is trip id, second trip start, and last is trip end
     */
    @Query("SELECT NEW com.javainiaisuzspringom.tripperis.dto.calendar.CalendarTripEntry" +
            "(t.id, t.dateFrom, t.dateTo) " +
            "FROM Account a " +
                "LEFT JOIN a.trips t " +
            "WHERE NOT (t.dateFrom > :periodEnd OR t.dateTo < :periodStart) " +
                "AND a = :account ")
    List<CalendarTripEntry> getTripDates(Account account, Timestamp periodStart, Timestamp periodEnd);

    Optional<Account> findByEmail(String username);
}