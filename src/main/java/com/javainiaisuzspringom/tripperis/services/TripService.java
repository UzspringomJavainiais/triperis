package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Autowired
    private TripRepository tripRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<TripDuration> getTripDuration(Trip trip) {
        List<TripDuration> durationList = tripRepository.getDuration(trip);
        if(durationList.isEmpty()) {
            return Optional.empty();
        }
        if(durationList.size() != 1) {
            LOGGER.error("Duration list is not of size 1, but {}", durationList.size());
            throw new IllegalStateException("Illegal attempt to query trips");
        }
        return Optional.of(durationList.get(0));
    }

    public Optional<Trip> getById(Integer id) {
        return tripRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeTrip(Trip trip) {
        tripRepository.delete(trip);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean exists(Trip trip) {
        return tripRepository.exists(Example.of(trip));
    }
}
