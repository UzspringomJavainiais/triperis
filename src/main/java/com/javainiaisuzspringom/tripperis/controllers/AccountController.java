package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

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

        Account account = accountResultById.get();

        List<CalendarEntry> accountFreeDates = accountService.getAccountCalendar(account.convertToDTO(), dateStart, dateEnd);
        return new ResponseEntity<>(accountFreeDates, HttpStatus.OK);
    }
}
