package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.domain.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }
}
