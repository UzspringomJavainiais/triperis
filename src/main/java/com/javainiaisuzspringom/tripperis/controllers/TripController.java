package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.csv.CsvService;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripService tripService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private CsvService csvService;

    @GetMapping("/api/trip")
    public List<TripDTO> getAllTrips() {
        return tripService.getAll();
    }

    @PostMapping("/api/trip")
    public ResponseEntity<TripDTO> addTrip(@RequestBody TripDTO trip) {
        TripDTO savedEntity = tripService.save(trip);

        /**
        for (Integer id : savedEntity.getAccounts()) {
            Optional<AccountDTO> account = accountService.getById(id);

            if (account.isPresent())
                account.get().getTripRequestIds();
        }
         */

        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
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

    @DeleteMapping("/api/trip/{id}")
    public ResponseEntity removeTrip(@PathVariable Integer id) {
        if(id == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Optional<TripDTO> trip = tripService.getById(id);

        if(!trip.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        tripService.removeTrip(trip.get());

        if (tripService.exists(trip.get())) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/trip/merge/{idOne}&{idTwo}")
    public ResponseEntity<TripDTO> mergeTrips(@PathVariable Integer idOne,
                                              @PathVariable Integer idTwo) {
        Optional<TripDTO> tripOneOptional = tripService.getById(idOne);
        Optional<TripDTO> tripTwoOptional = tripService.getById(idTwo);

        if (!tripOneOptional.isPresent() || !tripTwoOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        TripDTO tripOne = tripOneOptional.get();
        TripDTO tripTwo = tripTwoOptional.get();
        TripDTO mergedTrip = new TripDTO();

        mergedTrip.setName(tripOne + " & " + tripTwo);
        mergedTrip.setDescription("Trip \"" + tripOne.getName() + "\" merged with \"" + tripTwo.getName() + "\"");

        // Add distinct accounts to the merged trip
        mergedTrip.setAccounts(tripOne.getAccounts());

        for (Integer accountId : tripTwo.getAccounts()) {
            if (!mergedTrip.getAccounts().contains(accountId))
                mergedTrip.getAccounts().add(accountId);
        }

        // Merge distinct checklist items
        mergedTrip.setItems(tripOne.getItems());

        for (ChecklistItemDTO item : tripTwo.getItems()) {
            if (mergedTrip.getItems().contains(item))
                mergedTrip.getItems().add(item);
        }

        // TODO: mergedTrip.setStatus();

        tripService.save(mergedTrip);

        return new ResponseEntity<>(mergedTrip, HttpStatus.CREATED);
    }

    @GetMapping("api/trip/{id}/employees")
    public ResponseEntity<List<AccountDTO>> getEmployeesByTrip(@PathVariable Integer id) {
        Optional<TripDTO> trip = tripService.getById(id);

        if (!trip.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<AccountDTO> accountsInTrip = new ArrayList<>();

        for (Integer accountId : trip.get().getAccounts()) {
            Optional<AccountDTO> account = accountService.getById(accountId);

            account.ifPresent(accountsInTrip::add);
        }

        return new ResponseEntity<>(accountsInTrip, HttpStatus.OK);
    }

    @PostMapping("api/trip/{id}/addOrganizer/{organizerId}")
    public ResponseEntity<TripDTO> addOrganizerToTrip(@PathVariable Integer id,
                                                      @PathVariable Integer organizerId) {
        Optional<TripDTO> trip = tripService.getById(id);

        if (!trip.isPresent())
            return ResponseEntity.notFound().build();

        Optional<AccountDTO> account = accountService.getById(organizerId);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        TripDTO tripDTO = trip.get();
        AccountDTO accountDTO = account.get();
        if (!tripDTO.getOrganizers().contains(accountDTO.getId())) {
            tripDTO.getOrganizers().add(accountDTO.getId());
            tripService.save(tripDTO);
        }

        return new ResponseEntity<>(trip.get(), HttpStatus.OK);
    }

    @GetMapping("api/trip/{id}/organizers")
    public ResponseEntity<List<AccountDTO>> getOrganizersByTrip(@PathVariable Integer id,
                                                                @PathVariable Integer organizerId) {
        Optional<TripDTO> trip = tripService.getById(id);

        if (!trip.isPresent())
            return ResponseEntity.notFound().build();

        List<AccountDTO> organizers = new ArrayList<>();

        for (Integer accountId : trip.get().getOrganizers()) {
            Optional<AccountDTO> account = accountService.getById(accountId);

            account.ifPresent(organizers::add);
        }

        return new ResponseEntity<>(organizers, HttpStatus.OK);
    }

    @PostMapping("api/trip/{id}/removeOrganizer/{organizerId}")
    public ResponseEntity<TripDTO> removeOrganizerFromTrip(@PathVariable Integer id,
                                                           @PathVariable Integer organizerId) {
        Optional<TripDTO> trip = tripService.getById(id);

        if (!trip.isPresent())
            return ResponseEntity.notFound().build();

        Optional<AccountDTO> account = accountService.getById(organizerId);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        AccountDTO accountDTO = account.get();
        TripDTO tripDTO = trip.get();

        tripDTO.getOrganizers().remove(accountDTO.getId());
        tripService.save(tripDTO);

        return new ResponseEntity<>(tripDTO, HttpStatus.OK);
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
