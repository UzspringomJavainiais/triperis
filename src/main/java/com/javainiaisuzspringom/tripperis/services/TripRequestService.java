package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.TripRequestPatchDTO;
import com.javainiaisuzspringom.tripperis.repositories.TripRequestRepository;
import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.domain.TripRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripRequestService {

    @Autowired
    TripRequestRepository tripRequestRepository;

    public List<TripRequest> getMyPendingRequests(UserDetails userDetails) {
        return tripRequestRepository.getAllByStatusAndAccount_Email(TripRequestStatus.NEW, userDetails.getUsername());
    }

    public List<TripRequest> getMyPendingRequestByTripId(UserDetails userDetails, Integer tripId) {
        return tripRequestRepository.getAllByStatusAndAccount_EmailAndTripId(TripRequestStatus.NEW, userDetails.getUsername(), tripId);
    }

    public TripRequest patchTripRequest(TripRequestPatchDTO tripRequestPatchDTO) {
        TripRequest tripRequest = tripRequestRepository.getOne(tripRequestPatchDTO.getTripRequestId());
        tripRequest.setStatus(tripRequestPatchDTO.getDecision());
        tripRequestRepository.save(tripRequest);
        return tripRequest;
    }
}
