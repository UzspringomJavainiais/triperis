package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.*;
import com.javainiaisuzspringom.tripperis.dto.TripReservationRequest;
import com.javainiaisuzspringom.tripperis.dto.TripReservationResponse;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarTripEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.services.*;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;
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
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentUsageService apartmentUsageService;

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
    @Transactional
    public ResponseEntity reserveTrip(@RequestBody TripReservationRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        List<Account> accounts = request.getAccounts().stream().map(accountFromRequest -> accountService.getById(accountFromRequest.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Timestamp dateFrom = request.getDateFrom();
        Timestamp dateTo = request.getDateTo();
        Optional<Apartment> maybeApartmentTo = apartmentRepository.findById(request.getTo());
        if(request.getFrom() != null && !apartmentRepository.existsById(request.getFrom())) {
            return ResponseEntity.badRequest().body(String.format("Apartment with id %s does not exist", request.getFrom()));
        }
        if(!maybeApartmentTo.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Apartment with id %s does not exist", request.getTo()));
        }

        List<Account> unavailablePeople = accounts.stream()
                .filter(acc -> !accountService.getAccountCalendar(acc, dateFrom, dateTo).isEmpty())
                .collect(Collectors.toList());
        if(!unavailablePeople.isEmpty()) {
            return ResponseEntity.badRequest().body("People with names" + unavailablePeople.stream()
                    .map(person -> person.getFirstName() + person.getLastName()).collect(Collectors.joining(", "))
                    + "are not available in period " + dateFrom + " - " + dateTo);
        }

        // Create trip from request info
        Trip trip = new Trip();
        trip.setAccounts(accounts);
        trip.setName(request.getName());
        trip.setDescription(request.getDescription());
        trip.setDateFrom(dateFrom);
        trip.setDateTo(dateTo);
        request.getChecklistItems().forEach(trip::addChecklistItem);

        ApartmentUsage proposedUsage = new ApartmentUsage();
        proposedUsage.setFrom(dateFrom);
        proposedUsage.setTo(dateTo);
        proposedUsage.setApartment(maybeApartmentTo.get());

        trip.addUsage(proposedUsage);

        // Sukišimas
        Pair<List<RoomUsage>, List<Account>> listListPair = apartmentUsageService.autoAssignRooms(proposedUsage, accounts);

        addTrip(trip, userDetails);

        // Pervaliduojam
        apartmentUsageService.validateUsageToApartment(proposedUsage);

        // Saugojam
        tripRepository.saveAndFlush(trip);

        List<RoomUsageDTO> successfulReservations = listListPair.getLeft().stream().map(RoomUsage::convertToDTO).collect(Collectors.toList());
        List<Integer> unsuccessfulReservations = listListPair.getRight().stream().map(Account::getId).collect(Collectors.toList());

        return new ResponseEntity<>(new TripReservationResponse(dateFrom, dateTo, trip.getId(), request.getTo(), successfulReservations, unsuccessfulReservations), HttpStatus.OK);
    }

//    @PostMapping("/api/trip") // deprecated
    private Trip addTrip(@RequestBody Trip trip, @AuthenticationPrincipal UserDetails userDetails) {
        trip.setStatus(TripStatus.TRIP_CREATED);
        attachTripToEntities(trip);
        createTripRequests(trip);
        Account account = accountService.loadUserByUsername(userDetails.getUsername());
        trip.setOrganizers(Collections.singletonList(account));
        return trip;
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
//        persistedTrip.setTripAttachments();

        persistedTrip.getChecklistItems().forEach(item -> {
            item.setTrip(persistedTrip);
        });

        return tripRepository.save(persistedTrip);
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

        List<String> busyAccounts = new ArrayList<>();
        for (Account account : accounts) {
            List<CalendarEntry> entries = accountService.getAccountCalendar(account, dateFrom, dateTo);
            List<CalendarTripEntry> tripIdsThatUserIsIn = entries.stream()
                    .filter(calendar -> calendar instanceof CalendarTripEntry)
                    .map(entry -> (CalendarTripEntry) entry)
                    .collect(Collectors.toList());

            if (!entries.isEmpty()) {
                for (CalendarTripEntry entry : tripIdsThatUserIsIn) {
                    if (!tripOne.getId().equals(entry.getTripId()) && !tripTwo.getId().equals(entry.getTripId())) {
                        busyAccounts.add(account.getFirstName() + " " + account.getLastName() + " is busy between " + entry.getEnd().toString() + " and " + entry.getEnd());
                        break;
                    }
                }
            }
        }

        if (!busyAccounts.isEmpty())
            return ResponseEntity.badRequest().body(busyAccounts);

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
            if (item.getChecked())
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
        trip.getChecklistItems().forEach(checklistItem -> checklistItem.setTrip(trip));
    }
}
