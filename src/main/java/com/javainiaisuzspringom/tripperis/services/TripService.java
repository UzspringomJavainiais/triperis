package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Getter
    @Autowired
    private TripRepository repository;
    @Autowired
    private EntityManager em;

    public Trip getTripById(Integer tripId) {
        return repository.getOne(tripId);
    }

    public Trip save(Trip trip) {
        return repository.save(trip);
    }

    public Optional<TripDuration> getTripDuration(Trip trip) {
        List<TripDuration> durationList = repository.getDuration(trip);
        if (durationList.isEmpty()) {
            return Optional.empty();
        }
        if (durationList.size() != 1) {
            LOGGER.error("Duration list is not of size 1, but {}", durationList.size());
            throw new IllegalStateException("Illegal attempt to query trips");
        }
        return Optional.of(durationList.get(0));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Trip> getAll() {
        return repository.findAll();
    }

    public List<Trip> getMergableTrips(Trip trip) {
        return em.createNativeQuery("SELECT t.* " +
                "FROM trip t " +
                "WHERE t.id <> :id " +
                "AND ABS(DATE_PART('day', t.date_from - :dateFrom)) <= 1 " +
                "AND ABS(DATE_PART('day', t.date_to - :dateTo)) <= 1")
                .setParameter("id", trip.getId())
                .setParameter("dateFrom", trip.getDateFrom())
                .setParameter("dateTo", trip.getDateTo())
                .getResultList();
    }
}
