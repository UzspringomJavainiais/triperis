package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

}