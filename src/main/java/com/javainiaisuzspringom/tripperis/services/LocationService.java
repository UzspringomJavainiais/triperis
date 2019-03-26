package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Location;
import com.javainiaisuzspringom.tripperis.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
