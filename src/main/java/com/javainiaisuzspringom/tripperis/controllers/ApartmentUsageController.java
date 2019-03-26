package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.services.ApartmentUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApartmentUsageController {

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @GetMapping("/apartment-usage")
    public List<ApartmentUsage> getAllApartments() {
        return apartmentUsageService.getAllApartmentUsages();
    }

    @PostMapping("/apartment-usage")
    public ResponseEntity<ApartmentUsage> addApartment(@RequestBody ApartmentUsage apartment) {
        ApartmentUsage savedEntity = apartmentUsageService.save(apartment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
