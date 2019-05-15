package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
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

    @DeleteMapping("/api/trip/{id}")
    public ResponseEntity removeTrip(@PathVariable Integer id) {
        if(id == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if(!maybeTrip.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        tripRepository.delete(maybeTrip.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/trip/merge/{idOne}&{idTwo}")
    public ResponseEntity<TripDTO> mergeTrips(@PathVariable Integer idOne,
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
        mergedTrip.setItems(tripOne.getItems());

        for (ChecklistItem item : tripTwo.getItems()) {
            if (mergedTrip.getItems().contains(item))
                mergedTrip.getItems().add(item);
        }

        // TODO: mergedTrip.setStatus();

        tripRepository.save(mergedTrip);

        return new ResponseEntity<>(mergedTrip.convertToDTO(), HttpStatus.CREATED);
    }

    @GetMapping("api/trip/{id}/employees")
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
    public ResponseEntity<TripDTO> addOrganizerToTrip(@PathVariable Integer id,
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

        return new ResponseEntity<>(trip.convertToDTO(), HttpStatus.OK);
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
    public ResponseEntity<TripDTO> removeOrganizerFromTrip(@PathVariable Integer id,
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

        return new ResponseEntity<>(trip.convertToDTO(), HttpStatus.OK);
    }

    @GetMapping("/api/tripsInfo/csv")
    public void getTripsInfoCSV(HttpServletResponse response){
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Content-disposition", "attachment; filename="+ "trips.csv");
        csvService.createTripsCsv(response);
    }

    @GetMapping("/api/trip/{id}/progress")
    public ResponseEntity<Float> getProgress(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        int completedItems = 0, totalItems = 0;

        Trip trip = maybeTrip.get();

        for (ChecklistItem item : trip.getItems()) {
            if (item.isChecked())
                completedItems++;
        }

        totalItems = trip.getItems().size();

        return new ResponseEntity<>((float) (completedItems / totalItems), HttpStatus.OK);
    }
}
