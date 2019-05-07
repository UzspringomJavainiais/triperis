package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.LocationDTO;
import com.javainiaisuzspringom.tripperis.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/api/location")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAll();
    }

    @PostMapping("/api/location")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO location) {
        LocationDTO savedEntity = locationService.save(location);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
