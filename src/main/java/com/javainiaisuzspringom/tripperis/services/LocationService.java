package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Location;
import com.javainiaisuzspringom.tripperis.dto.entity.LocationDTO;
import com.javainiaisuzspringom.tripperis.repositories.LocationRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService implements BasicDtoToEntityService<Location, LocationDTO, Integer> {

    @Getter
    @Autowired
    private LocationRepository repository;

    public Location convertToEntity(LocationDTO dto) {
        Location location = new Location();

        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        location.setCountry(dto.getCountry());
        location.setGeocoord(dto.getGeocoord());
        location.setName(dto.getName());

        return location;
    }
}
