package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.*;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripRequestService;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private TripRequestService tripRequestService;

    @GetMapping("/api/trip")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @GetMapping("/api/trip/{id}")
    public Trip getTripById(@PathVariable Integer id) {
        return tripRepository.getOne(id);
    }

    @PostMapping("/api/trip")
    public Trip addTrip(@RequestBody Trip trip, @AuthenticationPrincipal UserDetails userDetails) {
        trip.setStatus(TripStatus.TRIP_CREATED);

        attachTripToEntities(trip);
        createTripRequests(trip);
        Account account = accountService.loadUserByUsername(userDetails.getUsername());
        trip.setOrganizers(Collections.singletonList(account));

        return tripRepository.save(trip);
    }

    @PostMapping("/api/trip/{id}/cancelTrip")
    public ResponseEntity<Trip> cancelTrip(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Trip trip = maybeTrip.get();

        trip.setStatus(TripStatus.TRIP_CANCELLED);

        return ResponseEntity.ok(trip);
    }

    @PostMapping("/api/trip/{id}/startTrip")
    public ResponseEntity<Trip> startTrip(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Trip trip = maybeTrip.get();

        trip.setStatus(TripStatus.TRIP_IN_PROGRESS);

        return ResponseEntity.ok(trip);
    }

    private void createTripRequests(Trip trip) {
        List<TripRequest> tripRequests = trip.getAccounts()
                .stream()
                .map(account -> createTripRequest(account, trip))
                .collect(Collectors.toList());

        trip.setTripRequests(tripRequests);
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

    @PutMapping("/api/trip")
    @Transactional
    public Trip updateTrip(@RequestBody Trip trip, @AuthenticationPrincipal UserDetails userDetails) {
        Trip persistedTrip = tripRepository.getOne(trip.getId());

        List<TripRequest> editTripRequests = tripRequestService.createEditTripRequests(persistedTrip, trip);

        persistedTrip.setName(trip.getName());
        persistedTrip.setDescription(trip.getDescription());
        persistedTrip.setStatus(trip.getStatus());
        persistedTrip.setDateFrom(trip.getDateFrom());
        persistedTrip.setDateTo(trip.getDateTo());

        persistedTrip.getTripRequests().addAll(editTripRequests);
        persistedTrip.setChecklistItems(trip.getChecklistItems());
        persistedTrip.setAccounts(trip.getAccounts());
//        persistedTrip.setTripSteps();
//        persistedTrip.setTripAttachments();

        persistedTrip.getChecklistItems().forEach(item -> {
            item.setTrip(persistedTrip);
        });

        return tripRepository.save(persistedTrip);
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

    @GetMapping("/api/trip/{id}/getTotalPrice")
    public ResponseEntity<BigDecimal> getTotalPrice(@PathVariable Integer id) {
        Optional<Trip> tripResultById = tripRepository.findById(id);
        if (!tripResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripResultById.get();
        List<ChecklistItem> checklistItems = trip.getChecklistItems();

        BigDecimal result = checklistItems.stream()
                .map(ChecklistItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/trip/{id}/tripsForMerge")
    public ResponseEntity<List<Trip>> getTripsAvailableForMerge(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Trip trip = maybeTrip.get();

        return ResponseEntity.ok(tripService.getMergableTrips(trip));
    }


    @PostMapping("/api/trip/merge/{idOne}&{idTwo}")
    public ResponseEntity mergeTrips(@PathVariable Integer idOne,
                                           @PathVariable Integer idTwo) {
        Optional<Trip> tripOneOptional = tripRepository.findById(idOne);
        Optional<Trip> tripTwoOptional = tripRepository.findById(idTwo);

        if (!tripOneOptional.isPresent() || !tripTwoOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Trip tripOne = tripOneOptional.get();
        Trip tripTwo = tripTwoOptional.get();

        // Check if the trips can be merged
        if (ChronoUnit.DAYS.between(tripOne.getDateFrom().toInstant(), tripTwo.getDateFrom().toInstant()) > 1
                || ChronoUnit.DAYS.between(tripOne.getDateTo().toInstant(), tripTwo.getDateTo().toInstant()) > 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Timestamp dateFrom, dateTo;

        // Set the earliest date from
        if (tripOne.getDateFrom().toInstant().isBefore(tripTwo.getDateFrom().toInstant()))
            dateFrom = tripOne.getDateFrom();
        else
            dateFrom = tripTwo.getDateFrom();

        // Do the same for the date to
        if (tripOne.getDateTo().toInstant().isAfter(tripTwo.getDateTo().toInstant()))
            dateTo = tripOne.getDateTo();
        else
            dateTo = tripTwo.getDateTo();

        // Check if accounts can participate in the event
        List<Account> accounts = new ArrayList<>();

        for (Account account : tripOne.getAccounts())
            accounts.add(account);

        for (Account account : tripTwo.getAccounts()) {
            if (!accounts.contains(account))
                accounts.add(account);
        }

        for (Account account : accounts) {
            List<CalendarEntry> entries = accountService.getAccountCalendar(account, dateFrom, dateTo);

            if (!entries.isEmpty())
                return ResponseEntity.badRequest().body(account.getFirstName() + " " + account.getLastName() + " is busy between " + dateFrom.toString() + " and " + dateTo.toString());
        }

        Trip mergedTrip = new Trip();

        mergedTrip.setName(tripOne.getName() + " & " + tripTwo.getName());
        mergedTrip.setDescription("Trip \"" + tripOne.getName() + "\" merged with \"" + tripTwo.getName() + "\"");
        mergedTrip.setDateFrom(dateFrom);
        mergedTrip.setDateTo(dateTo);

        // Add distinct accounts to the merged trip
        mergedTrip.setAccounts(accounts);

        // Merge distinct checklist items
        for (ChecklistItem item : tripOne.getChecklistItems())
            mergedTrip.getChecklistItems().add(item);

        for (ChecklistItem item : tripTwo.getChecklistItems()) {
            if (!mergedTrip.getChecklistItems().contains(item))
                mergedTrip.getChecklistItems().add(item);
        }

        // Merge distinct organizers
        for (Account account : tripOne.getOrganizers())
            mergedTrip.getOrganizers().add(account);

        for (Account organizer : tripTwo.getOrganizers()) {
            if (!mergedTrip.getOrganizers().contains(organizer))
                mergedTrip.getOrganizers().add(organizer);
        }

        // TODO: mergedTrip.setType();

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

    @GetMapping("/api/trip/requests/{id}")
    public List<TripRequest> getTripRequestsByTripId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        return tripRequestService.getMyPendingRequestByTripId(userDetails, id);
    }

    @GetMapping("/api/trip/{id}/apartmentUsage")
    public ResponseEntity<List<ApartmentUsage>> getApartmentUsageByTripId(@PathVariable Integer id) {
        Optional<Trip> maybeTrip = tripRepository.findById(id);

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        Trip trip = maybeTrip.get();
        
        return ResponseEntity.ok(new ArrayList<>(trip.getTripApartmentUsages()));
    }

    @PatchMapping("/api/trip/requests")
    public TripRequest patchTripRequest(@RequestBody TripRequestPatchDTO tripRequestPatchDTO) {
        return tripRequestService.patchTripRequest(tripRequestPatchDTO);
    }

    private TripRequest createTripRequest(Account account, Trip trip) {
        TripRequest tripRequest = new TripRequest();
        tripRequest.setAccount(account);
        tripRequest.setType(TripRequestType.NEW_TRIP);
        tripRequest.setTrip(trip);
        tripRequest.setStatus(TripRequestStatus.NEW);
        return tripRequest;
    }

    private void attachTripToEntities(Trip trip) {
        trip.getTripSteps().forEach(tripStep -> tripStep.setTrip(trip));
        trip.getChecklistItems().forEach(checklistItem -> checklistItem.setTrip(trip));
    }
}
