package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query(value = "SELECT MIN(ts.startDate), MAX(ts.endDate) FROM Trip t LEFT JOIN t.tripSteps ts WHERE t = :trip")
    List<Object[]> getDuration(Trip trip);
}