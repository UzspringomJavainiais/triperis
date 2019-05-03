package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/apartment")
    public List<ApartmentDTO> getAllApartments() {
        return apartmentService.getAll();
    }

    @PostMapping("/apartment")
    public ResponseEntity<ApartmentDTO> addApartment(@RequestBody ApartmentDTO apartment) {
        ApartmentDTO savedEntity = apartmentService.save(apartment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
