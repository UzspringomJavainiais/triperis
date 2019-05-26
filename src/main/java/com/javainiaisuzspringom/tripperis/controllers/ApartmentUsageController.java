package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.domain.Room;
import com.javainiaisuzspringom.tripperis.dto.ReservationInfo;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomRepository;
import com.javainiaisuzspringom.tripperis.services.ApartmentUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ApartmentUsageController {

    @Autowired
    private ApartmentUsageService apartmentUsageService;

    @Autowired
    private ApartmentUsageRepository apartmentUsageRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/api/apartment/usage")
    public List<ApartmentUsageDTO> getAllApartmentUsages() {
        return apartmentUsageRepository.findAll().stream()
                .map(ApartmentUsage::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/apartment/{id}/usage")
    public ResponseEntity<List<ApartmentUsageDTO>> getUsagesForOfApartment(@PathVariable(name= "id") Integer id) {
        Optional<Apartment> maybeApartment = apartmentRepository.findById(id);
        if(!maybeApartment.isPresent()) {
            throw new IllegalStateException("Apartment doesn't exist");
        }

        Apartment apartment = maybeApartment.get();

        List<ApartmentUsageDTO> usagesForApartment = apartmentUsageRepository.findUsagesForApartment(apartment).stream()
                .map(ApartmentUsage::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(usagesForApartment, HttpStatus.OK);
    }

    @PostMapping("/api/apartment/{id}/usage")
    public ResponseEntity<ApartmentUsageDTO> addApartmentUsageToApartment(@PathVariable(name= "id") Integer id, @RequestBody ApartmentUsageDTO apartmentUsage) {
        Optional<Apartment> maybeApartment = apartmentRepository.findById(id);
        if(!maybeApartment.isPresent()) {
            throw new IllegalStateException("Apartment doesn't exist");
        }
        // or simply set it to null?
        if(!apartmentUsage.getApartmentId().equals(id) || apartmentUsage.getApartmentId() != null) {
            throw new IllegalStateException(String.format("Apartment id should not be declared in the payload, or should be equal to the path parameter %d != %d", apartmentUsage.getApartmentId(), id));
        }

        Apartment apartment = maybeApartment.get();
        ApartmentUsage apartmentUsageEntity = apartmentUsageService.getExistingOrConvert(apartmentUsage);
        apartmentUsageService.validateUsageToApartment(apartmentUsageEntity);
        apartment.addApartmentUsage(apartmentUsageEntity);

        ApartmentUsage savedEntity = apartmentUsageService.save(apartmentUsageEntity.convertToDTO());
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }

    @GetMapping("/api/apartment/{id}/room/{roomId}")
    public List<ReservationInfo> getReservationsForUsage(@PathVariable(name= "id") Integer id,
                                                         @PathVariable(name= "roomId") Integer roomId,
                                                         @RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                                                         @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd) {
        Optional<Apartment> maybeApartment = apartmentRepository.findById(id);
        if(!maybeApartment.isPresent()) {
            throw new IllegalStateException("Apartment doesn't exist");
        }

        Optional<Room> maybeRoom = roomRepository.findByIdAndApartment(roomId, maybeApartment.get());
        if(!maybeRoom.isPresent()) {
            throw new IllegalStateException("Room doesn't exist");
        }
        return apartmentUsageService.getCapacityListForRoom(maybeRoom.get(), dateStart, dateEnd);
    }
}
