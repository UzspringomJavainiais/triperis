package com.javainiaisuzspringom.tripperis.apartment;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/apartment")
    public List<Apartment> getAllApartments() {
        return apartmentService.getAllApartments();
    }

    @PostMapping("/apartment")
    public ResponseEntity addApartment(@RequestBody Apartment apartment) {
        apartmentService.save(apartment);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
