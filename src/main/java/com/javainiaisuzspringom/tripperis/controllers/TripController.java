package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
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
@CrossOrigin
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CsvService csvService;

    @GetMapping("/api/trip")
    public List<Trip> getAllTrips() {
        return tripService.getAll();
    }

    @GetMapping("/api/trip/{id}")
    public Trip getTripById(@PathVariable Integer id) {
        return tripService.getTripById(id);
    }

    @PostMapping("/api/trip")
    public Trip addTrip(@RequestBody Trip trip) {
        attachTripToEntities(trip);
        return tripService.save(trip);
    }

    @DeleteMapping("/api/trip/{id}")
    public ResponseEntity removeTrip(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        tripRepository.delete(maybeTrip.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/trip/{id}/getTotalDuration")
    public ResponseEntity<TripDuration> getTotalDuration(@PathVariable Integer id) {
        Optional<Trip> tripResultById = tripRepository.findById(id);
        if (!tripResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripResultById.get();
        Optional<TripDuration> tripStartDate = tripService.getTripDuration(trip);
        if (!tripStartDate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tripStartDate.get());
    }


    @PostMapping("/api/trip/merge/{idOne}&{idTwo}")
    public ResponseEntity<Trip> mergeTrips(@PathVariable Integer idOne,
                                           @PathVariable Integer idTwo) {
        Optional<Trip> tripOneOptional = tripRepository.findById(idOne);
        Optional<Trip> tripTwoOptional = tripRepository.findById(idTwo);

        if (!tripOneOptional.isPresent() || !tripTwoOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Trip tripOne = tripOneOptional.get();
        Trip tripTwo = tripTwoOptional.get();
        Trip mergedTrip = new Trip();

        mergedTrip.setName(tripOne + " & " + tripTwo);
        mergedTrip.setDescription("Trip \"" + tripOne.getName() + "\" merged with \"" + tripTwo.getName() + "\"");

        // Add distinct accounts to the merged trip
        mergedTrip.setAccounts(tripOne.getAccounts());

        for (Account account : tripTwo.getAccounts()) {
            if (!mergedTrip.getAccounts().contains(account))
                mergedTrip.getAccounts().add(account);
        }

        // Merge distinct checklist items
        mergedTrip.setChecklistItems(tripOne.getChecklistItems());

        for (ChecklistItem item : tripTwo.getChecklistItems()) {
            if (mergedTrip.getChecklistItems().contains(item))
                mergedTrip.getChecklistItems().add(item);
        }

        // TODO: mergedTrip.setStatus();

        tripRepository.save(mergedTrip);

        return new ResponseEntity<>(mergedTrip, HttpStatus.CREATED);
    }

    @GetMapping("/api/trip/{id}/employees")
    public ResponseEntity<List<AccountDTO>> getEmployeesByTrip(@PathVariable Integer id) {
        Optional<Trip> trip = tripRepository.findById(id);

        if (!trip.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<AccountDTO> accountsInTrip = trip.get().getAccounts().stream()
                .map(Account::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(accountsInTrip, HttpStatus.OK);
    }

    @PostMapping("api/trip/{id}/addOrganizer/{organizerId}")
    public ResponseEntity<Trip> addOrganizerToTrip(@PathVariable Integer id,
                                                   @PathVariable Integer organizerId) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Optional<Account> maybeAccount = accountService.getById(organizerId);

        if (!maybeAccount.isPresent())
            return ResponseEntity.notFound().build();

        Trip trip = maybeTrip.get();
        Account account = maybeAccount.get();
        if (!trip.getOrganizers().contains(account)) {
            trip.getOrganizers().add(account);
            tripRepository.save(trip);
        }

        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @GetMapping("api/trip/{id}/organizers")
    public ResponseEntity<List<AccountDTO>> getOrganizersByTrip(@PathVariable Integer id,
                                                                @PathVariable Integer organizerId) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        List<AccountDTO> organizers = maybeTrip.get().getOrganizers().stream()
                .map(Account::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(organizers, HttpStatus.OK);
    }

    @PostMapping("api/trip/{id}/removeOrganizer/{organizerId}")
    public ResponseEntity<Trip> removeOrganizerFromTrip(@PathVariable Integer id,
                                                        @PathVariable Integer organizerId) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Optional<Account> maybeAccount = accountService.getById(organizerId);

        if (!maybeAccount.isPresent())
            return ResponseEntity.notFound().build();

        Account account = maybeAccount.get();
        Trip trip = maybeTrip.get();

        trip.getOrganizers().remove(account);
        tripRepository.save(trip);

        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @GetMapping("/api/tripsInfo/csv")
    public void getTripsInfoCSV(HttpServletResponse response) {
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Content-disposition", "attachment; filename=" + "trips.csv");
        csvService.createTripsCsv(response);
    }

    @GetMapping("/api/trip/{id}/progress")
    public ResponseEntity<Float> getProgress(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        int completedItems = 0, totalItems = 0;

        Trip trip = maybeTrip.get();

        for (ChecklistItem item : trip.getChecklistItems()) {
            if (item.isChecked())
                completedItems++;
        }

        totalItems = trip.getChecklistItems().size();

        return new ResponseEntity<>((float) (completedItems / totalItems), HttpStatus.OK);
    }

    private void attachTripToEntities(Trip trip) {
        trip.getTripSteps().forEach(tripStep -> tripStep.setTrip(trip));
        trip.getChecklistItems().forEach(checklistItem -> checklistItem.setTrip(trip));
    }
}
