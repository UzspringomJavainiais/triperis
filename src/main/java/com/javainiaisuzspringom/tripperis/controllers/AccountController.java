package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TripService tripService;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/api/account")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO account) {
        if (accountService.exists(account.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }
        if(account.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password should not be empty");
        }
        AccountDTO savedEntity = accountService.save(account);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @GetMapping("/api/account")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAll();
    }

    @GetMapping(value = "/api/account/{id}/tripsInPeriod")
    public ResponseEntity<List<CalendarEntry>> tripsInPeriod(@PathVariable(name = "id") Integer id,
                                                             @RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                                             @RequestParam(name = "dateEnd")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {

        Optional<Account> accountResultById = accountService.getById(id);

        if(!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AccountDTO account = accountResultById.get().convertToDTO();

        List<CalendarEntry> accountFreeDates = accountService.getAccountCalendar(account, dateStart, dateEnd);
        return new ResponseEntity<>(accountFreeDates, HttpStatus.OK);
    }

    @GetMapping("/api/account/{id}/trips")
    public ResponseEntity<List<TripDTO>> getTripsByAccount(@PathVariable Integer id) {
        Optional<Account> account = accountService.getById(id);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        AccountDTO accountDTO = account.get().convertToDTO();
        List<TripDTO> accountTrips = new ArrayList<>();
        for (Integer tripId : accountDTO.getTrips()) {
            Optional<TripDTO> tripDTO = tripService.getById(tripId);

            tripDTO.ifPresent(accountTrips::add);
        }

        return new ResponseEntity<>(accountTrips, HttpStatus.OK);
    }

    @PostMapping("/api/account/{id}/approveTrip/{tripId}")
    public ResponseEntity<Account> approveTrip(@PathVariable Integer id,
                                               @PathVariable Integer tripId) {
        Optional<Account> account = accountService.getById(id);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        Optional<TripDTO> trip = tripService.getById(tripId);

        if (!trip.isPresent())
            return ResponseEntity.notFound().build();

        AccountDTO accountDTO = account.get().convertToDTO();

        // TODO: Implement TripRequest search by Trip id

        throw new NotImplementedException();
    }
}
