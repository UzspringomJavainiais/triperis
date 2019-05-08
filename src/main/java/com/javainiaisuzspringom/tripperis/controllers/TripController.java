package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.controllers.util.MergeTrips;
import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private CsvService csvService;

    @GetMapping("/api/trip")
    public List<TripDTO> getAllTrips() {
        return tripService.getAll().stream()
                .map(Trip::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/trip")
    public ResponseEntity<TripDTO> addTrip(@RequestBody TripDTO trip) {
        Trip savedEntity = tripService.save(trip);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }

    /**
     * Returns a single {@link TripDuration} for a given {@link Trip}.
     * Trip duration start is the smallest {@link TripStep#getStartDate()}
     * and the duration end is the biggest {@link TripStep#getEndDate()}
     *
     * @param id id of Trip
     * @return trip duration for given trip, if trip is found. Else return a not found response
     */
    @GetMapping("/api/trip/{id}/getTotalDuration")
    public ResponseEntity<TripDuration> getTotalDuration(@PathVariable Integer id) {
        Optional<TripDTO> tripResultById = tripService.getById(id);
        if (!tripResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TripDTO trip = tripResultById.get();
        Optional<TripDuration> tripStartDate = tripService.getTripDuration(trip);
        if (!tripStartDate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tripStartDate.get());
    }

    @PostMapping("/api/tripRemove")
    public ResponseEntity removeTrip(@RequestBody TripDTO trip) {
        if(trip.getId() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(!tripService.exists(trip)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        tripService.removeTrip(trip);

        if (tripService.exists(trip)) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/tripMerge")
    public ResponseEntity<TripDTO> mergeTrips(@RequestBody MergeTrips mergeTrips) {
        TripDTO mergedTrip = new TripDTO();

        mergedTrip.setName(mergeTrips.getName());
        mergedTrip.setDescription(mergedTrip.getDescription());

        // Add distinct accounts to the merged trip
        mergedTrip.setAccounts(mergeTrips.getTripOne().getAccounts());

        for (Integer accountId : mergeTrips.getTripTwo().getAccounts()) {
            if (!mergedTrip.getAccounts().contains(accountId))
                mergedTrip.getAccounts().add(accountId);
        }

        // Merge distinct checklist items
        mergedTrip.setItems(mergeTrips.getTripOne().getItems());

        for (ChecklistItemDTO item : mergeTrips.getTripTwo().getItems()) {
            if (mergedTrip.getItems().contains(item))
                mergedTrip.getItems().add(item);
        }

        // TODO: mergedTrip.setStatus();

        tripService.save(mergedTrip);

        return new ResponseEntity<>(mergedTrip, HttpStatus.CREATED);
    }

    @GetMapping("/api/tripsInfo/csv")
    public void getTripsInfoCSV(HttpServletResponse response){
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Content-disposition", "attachment; filename="+ "trips.csv");
        csvService.createTripsCsv(response);
    }

    public ResponseEntity<Float> getProgress(@RequestBody TripDTO trip) {
        int completedItems = 0, totalItems = 0;

        for (ChecklistItemDTO item : trip.getItems()) {
            if (item.isChecked())
                completedItems++;
        }

        totalItems = trip.getItems().size();

        return new ResponseEntity<>((float) (completedItems / totalItems), HttpStatus.OK);
    }
}
