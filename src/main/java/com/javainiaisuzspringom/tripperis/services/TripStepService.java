package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.repositories.TripStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripStepService {

    @Autowired
    private TripStepRepository tripStepRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public TripStep save(TripStep tripStep) {
        return tripStepRepository.save(tripStep);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<TripStep> getAllTripSteps() {
        return tripStepRepository.findAll();
    }
}
