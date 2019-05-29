package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

    @Query("SELECT SUM(room.maxCapacity) " +
            "FROM Apartment a LEFT JOIN a.rooms room " +
            "WHERE a = :apartment")
    Long getCapacity(Apartment apartment);
}