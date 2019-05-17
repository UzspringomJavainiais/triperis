package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentUsageRepository extends JpaRepository<ApartmentUsage, Integer> {

    @Query("SELECT au FROM ApartmentUsage au WHERE au.apartment = :apartment")
    List<ApartmentUsage> findUsagesForApartment(Apartment apartment);
}