package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
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
    public List<ApartmentUsageDTO> getAllApartments() {
        return apartmentUsageService.getAll();
    }

    @PostMapping("/apartment-usage")
    public ResponseEntity<ApartmentUsageDTO> addApartment(@RequestBody ApartmentUsageDTO apartment) {
        ApartmentUsageDTO savedEntity = apartmentUsageService.save(apartment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
