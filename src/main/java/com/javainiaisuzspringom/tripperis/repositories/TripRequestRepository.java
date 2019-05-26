package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.domain.TripRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRequestRepository extends JpaRepository<TripRequest, Integer> {

    List<TripRequest> getAllByStatusAndAccount_Email(TripRequestStatus status, String accountUsername);

    List<TripRequest> getAllByStatusAndAccount_EmailAndTripId(TripRequestStatus status, String accountUsername, Integer tripId);

}