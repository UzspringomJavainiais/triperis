package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.services.ApartmentService;
import com.javainiaisuzspringom.tripperis.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private RoomService roomService;

    @GetMapping("/api/apartment")
    public List<ApartmentDTO> getAllApartments() {
        return apartmentService.getAll();
    }

    @PostMapping("/api/apartment")
    public ResponseEntity<ApartmentDTO> addApartment(@RequestBody ApartmentDTO apartment) {
        ApartmentDTO savedEntity = apartmentService.save(apartment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @PostMapping("/api/apartment/{id}/rooms")
    public ResponseEntity<ApartmentDTO> addRooms(@PathVariable(name = "id") Integer id, @RequestBody List<RoomDTO> rooms) {
        Optional<Apartment> maybeApartment = apartmentRepository.findById(id);
        if(!maybeApartment.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Apartment apartment = maybeApartment.get();
        rooms.stream()
                .map(dto -> roomService.getExistingOrConvert(dto))
                .forEach(apartment::addRoom);
        apartmentRepository.save(apartment);
        return new ResponseEntity<>(apartment.convertToDTO(), HttpStatus.CREATED);
    }
}
