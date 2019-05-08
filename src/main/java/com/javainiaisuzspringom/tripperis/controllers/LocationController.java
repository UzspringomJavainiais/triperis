package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Location;
import com.javainiaisuzspringom.tripperis.dto.entity.LocationDTO;
import com.javainiaisuzspringom.tripperis.services.LocationService;
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
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/api/location")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAll().stream()
                .map(Location::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/location")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO location) {
        Location savedEntity = locationService.save(location);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
