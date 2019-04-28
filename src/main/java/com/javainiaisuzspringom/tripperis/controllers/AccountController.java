package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/account")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/account")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        Account savedEntity = accountService.save(account);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
