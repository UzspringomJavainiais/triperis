package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<TripDuration> getTripStartDate(Trip trip) {
        // I don't know how to force the query to return objects of wanted type, so we have to do this stuff right here
        List<Object[]> objectList = tripRepository.getStartDate(trip);
        if(objectList.size() != 1) {
            LOGGER.error("Object list holding is not of size 1, but {}", objectList.size());
            throw new IllegalStateException("Illegal attempt to query trips");
        }
        Object[] timestampObjs = objectList.get(0);
        if(timestampObjs.length == 0) {
            return Optional.empty();
        }
        if(timestampObjs.length != 2) {
            throw new IllegalStateException(String.format("Trip has %d start and end dates", timestampObjs.length));
        }

        return Optional.of(new TripDuration(trip.getId(), (Timestamp) timestampObjs[0], (Timestamp) timestampObjs[1]));
    }

    public Optional<Trip> getById(Integer id) {
        return tripRepository.findById(id);
    }
}
