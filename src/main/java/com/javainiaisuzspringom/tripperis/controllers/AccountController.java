package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TripService tripService;

    @PostMapping("/api/account")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO account) {
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

        Optional<AccountDTO> accountResultById = accountService.getById(id);

        if(!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AccountDTO account = accountResultById.get();

        List<CalendarEntry> accountFreeDates = accountService.getAccountCalendar(account, dateStart, dateEnd);
        return new ResponseEntity<>(accountFreeDates, HttpStatus.OK);
    }

    @PostMapping("/api/account/{id}/approveTrip/{tripId}")
    public ResponseEntity<Account> approveTrip(@PathVariable Integer id,
                                               @PathVariable Integer tripId) {
        Optional<AccountDTO> account = accountService.getById(id);

        if (!account.isPresent())
            return ResponseEntity.notFound().build();

        Optional<TripDTO> trip = tripService.getById(tripId);

        if (!trip.isPresent())
            return ResponseEntity.notFound().build();

        AccountDTO accountDTO = account.get();

        // TODO: Implement TripRequest search by Trip id

        throw new NotImplementedException();
    }
}
