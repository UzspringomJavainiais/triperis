package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.domain.Room;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.LocationRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ApartmentService extends AbstractBasicEntityService<Apartment, ApartmentDTO, Integer> {

    @Getter
    @Autowired
    private ApartmentRepository repository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @Autowired
    private RoomService roomService;

    protected Apartment convertToEntity(ApartmentDTO dto) {
        Apartment apartment = new Apartment();

        apartment.setName(dto.getName());
        apartment.setLocation(locationService.getExistingOrConvert(dto.getLocation()));
        apartment.setApartmentUsages(dto.getApartmentUsages().stream()
                .map(usage -> apartmentUsageService.getExistingOrConvert(usage)).collect(Collectors.toList()));
        apartment.setRooms(dto.getRooms().stream()
                .map(x -> roomService.getExistingOrConvert(x)).collect(Collectors.toList()));

        return apartment;
    }

    public Long getApartmentCapacity(Apartment apartment) {
        return repository.getCapacity(apartment);
    }
}
