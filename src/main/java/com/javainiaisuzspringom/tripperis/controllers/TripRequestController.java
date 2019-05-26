package com.javainiaisuzspringom.tripperis.controllers;


import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.domain.TripRequestPatchDTO;
import com.javainiaisuzspringom.tripperis.services.TripRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TripRequestController {

    @Autowired
    TripRequestService tripRequestService;

    @GetMapping("/api/tripRequests/{id}")
    public List<TripRequest> getTripRequestsByTripId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        return tripRequestService.getMyPendingRequestByTripId(userDetails, id);
    }

    @PatchMapping("/api/tripRequests")
    public TripRequest patchTripRequest(@RequestBody TripRequestPatchDTO tripRequestPatchDTO) {
        return tripRequestService.patchTripRequest(tripRequestPatchDTO);
    }
}
