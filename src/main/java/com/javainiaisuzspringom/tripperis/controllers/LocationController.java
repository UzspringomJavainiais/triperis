package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.LocationDTO;
import com.javainiaisuzspringom.tripperis.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/location")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping("/location")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO location) {
        LocationDTO savedEntity = locationService.save(location);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
