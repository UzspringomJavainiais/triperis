package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApartmentUsageService {

    @Autowired
    private ApartmentUsageRepository apartmentUsageRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public ApartmentUsage save(ApartmentUsage apartmentUsage) {
        return apartmentUsageRepository.save(apartmentUsage);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ApartmentUsage> getAllApartmentUsages() {
        return apartmentUsageRepository.findAll();
    }
}
