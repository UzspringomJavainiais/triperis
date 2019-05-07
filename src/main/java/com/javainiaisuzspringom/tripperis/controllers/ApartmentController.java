package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/api/apartment")
    public List<ApartmentDTO> getAllApartments() {
        return apartmentService.getAll();
    }

    @PostMapping("/api/apartment")
    public ResponseEntity<ApartmentDTO> addApartment(@RequestBody ApartmentDTO apartment) {
//        ApartmentDTO savedEntity = apartmentService.save(apartment);
        apartmentService.save(apartment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
