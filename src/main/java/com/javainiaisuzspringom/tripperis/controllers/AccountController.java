package com.javainiaisuzspringom.tripperis.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TripService tripService;

    @PostMapping("/account")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        Account savedEntity = accountService.save(account);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @GetMapping("/account")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/account/{id}/isTakenInPeriod")
    public ResponseEntity<JsonNode> isAccountTaken(@PathVariable(name = "id") Integer id,
                                         @RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                         @RequestParam(name = "dateEnd")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {

        Optional<Account> accountResultById = accountService.getById(id);
        if(!accountResultById.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountResultById.get();
        boolean isTaken = account.getTrips().stream()
                .flatMap(trip -> trip.getTripSteps().stream())
                .map(step -> Pair.of(step.getStartDate(), step.getEndDate()))
                .anyMatch(startAndEndDates -> startAndEndDates.getLeft().before(dateStart)
                        && startAndEndDates.getRight().after(dateEnd));

        List<Timestamp> startDates = account.getTrips().stream()
                .flatMap(trip -> tripService.getTripStartDate(trip).stream())
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("isTaken", isTaken);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}
