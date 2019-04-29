package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.controllers.util.MergeTrips;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/trip")
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @PostMapping("/trip")
    public ResponseEntity<Trip> addTrip(@RequestBody Trip trip) {
        Trip savedEntity = tripService.save(trip);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @PostMapping("/tripRemove")
    public ResponseEntity removeTrip(@RequestBody Trip trip) {
        tripService.removeTrip(trip);

        if (tripService.exists(trip)) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/tripMerge")
    public ResponseEntity<Trip> mergeTrips(@RequestBody MergeTrips mergeTrips) {
        Trip mergedTrip = new Trip();

        mergedTrip.setName(mergeTrips.getName());
        mergedTrip.setDescription(mergedTrip.getDescription());

        // Add distinct accounts to the merged trip
        mergedTrip.setAccounts(mergeTrips.getTripOne().getAccounts());

        for (Account account : mergeTrips.getTripTwo().getAccounts()) {
            if (!mergedTrip.getAccounts().contains(account))
                mergedTrip.getAccounts().add(account);
        }

        // Merge distinct checklist items
        mergedTrip.setItems(mergeTrips.getTripOne().getItems());

        for (ChecklistItem item : mergeTrips.getTripTwo().getItems()) {
            if (mergedTrip.getItems().contains(item))
                mergedTrip.getItems().add(item);
        }

        // TODO: mergedTrip.setStatus();

        tripService.save(mergedTrip);

        return new ResponseEntity<>(mergedTrip, HttpStatus.CREATED);
    }
}
