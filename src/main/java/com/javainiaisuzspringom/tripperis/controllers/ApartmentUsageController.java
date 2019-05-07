package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.services.ApartmentUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ApartmentUsageController {

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @GetMapping("/api/apartment-usage")
    public List<ApartmentUsageDTO> getAllApartments() {
        return apartmentUsageService.getAll();
    }

    @PostMapping("/api/apartment-usage")
    public ResponseEntity<ApartmentUsageDTO> addApartment(@RequestBody ApartmentUsageDTO apartment) {
        ApartmentUsageDTO savedEntity = apartmentUsageService.save(apartment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
