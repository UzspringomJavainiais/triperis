package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.entity.TripStepDTO;
import com.javainiaisuzspringom.tripperis.services.TripStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TripStepController {

    @Autowired
    private TripStepService tripStepService;

    @GetMapping("/api/trip-step")
    public List<TripStepDTO> getAllTripSteps() {
        return tripStepService.getAll().stream()
                .map(TripStep::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/trip-step")
    public ResponseEntity<TripStepDTO> addTripStep(@RequestBody TripStepDTO tripStep) {
        TripStep savedEntity = tripStepService.save(tripStep);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
