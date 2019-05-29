package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.*;
import com.javainiaisuzspringom.tripperis.repositories.TripRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<TripRequest> createEditTripRequests(Trip oldTrip, Trip newTrip) {
        List<TripRequest> tripRequests = new ArrayList<>();

        if(!oldTrip.getDateFrom().equals(newTrip.getDateFrom()) || !oldTrip.getDateTo().equals(newTrip.getDateTo())) {

            newTrip.getAccounts().forEach(account -> {
                TripRequest tripRequest = new TripRequest();
                tripRequest.setAccount(account);
                tripRequest.setType(TripRequestType.TRIP_DATE_CHANGED);
                tripRequest.setTrip(newTrip);
                tripRequest.setStatus(TripRequestStatus.NEW);
                tripRequests.add(tripRequest);
            });

        }
        return tripRequests;
    }

}
