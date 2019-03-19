package com.javainiaisuzspringom.tripperis.apartment;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }
}
