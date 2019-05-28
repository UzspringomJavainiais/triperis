package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ApartmentUsageRepository extends JpaRepository<ApartmentUsage, Integer> {

    @Query("SELECT au FROM ApartmentUsage au " +
            "WHERE ((au.from >= :from AND au.to <= :to) " +
            "OR (au.to > :from AND au.from < :to)) " +
            "AND au.apartment = :apartment")
    List<ApartmentUsage> findAllApartmentUsagesInPeriod(Apartment apartment, Timestamp from, Timestamp to);
}