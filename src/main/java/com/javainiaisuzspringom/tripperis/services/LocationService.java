package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Location;
import com.javainiaisuzspringom.tripperis.dto.LocationDTO;
import com.javainiaisuzspringom.tripperis.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public LocationDTO save(LocationDTO location) {
        Location entityFromDTO = getExistingOrConvert(location);
        Location savedEntity = locationRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(Location::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Location getExistingOrConvert(LocationDTO dto) {
        if (dto.getId() != null) {
            Optional<Location> maybeLocation = locationRepository.findById(dto.getId());
            if (maybeLocation.isPresent()) {
                return maybeLocation.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private Location convertToEntity(LocationDTO dto) {
        Location location = new Location();

//        location.setId(this.getId());
        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        location.setCountry(dto.getCountry());
        location.setGeocoord(dto.getGeocoord());
        location.setName(dto.getName());

        return location;
    }
}
