package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @Transactional(propagation = Propagation.REQUIRED)
    public ApartmentDTO save(ApartmentDTO apartment) {
        Apartment entityFromDTO = getExistingOrConvert(apartment);
        Apartment savedEntity = apartmentRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ApartmentDTO> getAllApartments() {
        return apartmentRepository.findAll().stream().map(Apartment::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public Apartment getExistingOrConvert(ApartmentDTO dto) {
        if (dto.getId() != null) {
            Optional<Apartment> maybeTripStep = apartmentRepository.findById(dto.getId());
            if (maybeTripStep.isPresent()) {
                return maybeTripStep.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private Apartment convertToEntity(ApartmentDTO dto) {
        Apartment apartment = new Apartment();

//        apartment.setId(dto.getId());
        apartment.setName(dto.getName());
        apartment.setMaxCapacity(dto.getMaxCapacity());
        apartment.setLocation(locationRepo.getOne(dto.getLocationId()));
        apartment.setApartmentUsages(dto.getApartmentUsages().stream()
                .map(usage -> apartmentUsageService.getExistingOrConvert(usage)).collect(Collectors.toList()));

        return apartment;
    }
}
