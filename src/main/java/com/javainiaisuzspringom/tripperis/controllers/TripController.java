package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    /**
     * Returns a single {@link TripDuration} for a given {@link Trip}.
     * Trip duration start is the smallest {@link TripStep#getStartDate()}
     * and the duration end is the biggest {@link TripStep#getEndDate()}
     * @param id id of Trip
     * @return trip duration for given trip, if trip is found. Else return a not found response
     */
    @GetMapping("/trip/{id}/getTotalDuration")
    public ResponseEntity<TripDuration> getTotalDuration(@PathVariable Integer id) {
        Optional<Trip> tripResultById = tripService.getById(id);
        if(!tripResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripResultById.get();
        Optional<TripDuration> tripStartDate = tripService.getTripDuration(trip);
        if(!tripStartDate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tripStartDate.get());
    }
}
