package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
    @Query("SELECT NEW com.javainiaisuzspringom.tripperis.dto.CalendarEntry" +
            "(t.id, MIN(ts.startDate), MAX(ts.endDate)) " +
            "FROM Account a " +
                "LEFT JOIN a.trips t " +
                "LEFT JOIN t.tripSteps ts " +
            "WHERE ((ts.startDate >= :periodStart AND ts.endDate <= :periodEnd) " +
                "OR (ts.endDate > :periodStart AND ts.startDate < :periodEnd)) " +
                "AND a = :account " +
            "GROUP BY t")
    List<CalendarEntry> getTripDates(Account account, Timestamp periodStart, Timestamp periodEnd);

}