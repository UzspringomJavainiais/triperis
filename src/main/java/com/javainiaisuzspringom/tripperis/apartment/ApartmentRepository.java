package com.javainiaisuzspringom.tripperis.apartment;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

}