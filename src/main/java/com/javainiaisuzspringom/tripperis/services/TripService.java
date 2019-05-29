package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Getter
    @Autowired
    private TripRepository repository;
    @Autowired
    private EntityManager em;

    @Autowired
    private TripStepService tripStepService;

    @Autowired
    private ChecklistItemService checklistItemService;

    @Autowired
    private AccountRepository accountRepo;

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

    public Trip convertToEntity(TripDTO dto) {
        Trip trip = new Trip();

        trip.setName(dto.getName());
        trip.setDescription(dto.getDescription());
        if(dto.getTripSteps() != null)
            trip.setStatus(dto.getTripStatus());
        trip.setAccounts(dto.getAccounts().stream().map(accountId -> accountRepo.getOne(accountId)).collect(Collectors.toList()));
        trip.setChecklistItems(dto.getItems().stream().map(itemDTO -> checklistItemService.getExistingOrConvert(itemDTO)).collect(Collectors.toList()));
        trip.setTripSteps(dto.getTripSteps().stream().map(tripStep -> tripStepService.getExistingOrConvert(tripStep)).collect(Collectors.toList()));

        return trip;
    }

    public List<Trip> getMergableTrips(Trip trip) {
        List<Integer> ids = em.createNativeQuery("SELECT t.id " +
                "FROM trip t " +
                "WHERE t.id <> :id " +
                "AND ABS(DATE_PART('day', t.date_from - :dateFrom)) <= 1 " +
                "AND ABS(DATE_PART('day', t.date_to - :dateTo)) <= 1")
                .setParameter("id", trip.getId())
                .setParameter("dateFrom", trip.getDateFrom())
                .setParameter("dateTo", trip.getDateTo())
                .getResultList();

        List<Trip> trips = new ArrayList<>();
        for (Integer id : ids) {
            Optional<Trip> maybeTrip = repository.findById(id);

            if (maybeTrip.isPresent())
                trips.add(maybeTrip.get());
        }

        return trips;
    }
}
