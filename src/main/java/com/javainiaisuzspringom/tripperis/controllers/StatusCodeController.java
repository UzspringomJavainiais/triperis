package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import com.javainiaisuzspringom.tripperis.services.StatusCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusCodeController {

    @Autowired
    private StatusCodeService statusCodeService;

    @GetMapping("/status-code")
    public List<StatusCode> getAllStatusCodes() {
        return statusCodeService.getAllStatusCodes();
    }

    @PostMapping("/status-code")
    public ResponseEntity<StatusCode> addStatusCode(@RequestBody StatusCode statusCode) {
        StatusCode savedEntity = statusCodeService.save(statusCode);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
