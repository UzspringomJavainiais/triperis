package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.services.ApartmentUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ApartmentUsageController {

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @GetMapping("/api/apartment-usage")
    public List<ApartmentUsageDTO> getAllApartments() {
        return apartmentUsageService.getAll().stream()
                .map(ApartmentUsage::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/apartment-usage")
    public ResponseEntity<ApartmentUsageDTO> addApartment(@RequestBody ApartmentUsageDTO apartment) {
        ApartmentUsage savedEntity = apartmentUsageService.save(apartment);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
