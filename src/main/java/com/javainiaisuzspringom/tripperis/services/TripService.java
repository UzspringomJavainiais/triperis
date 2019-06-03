package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Getter;
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

    @Getter
    @Autowired
    private TripRepository repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private ChecklistItemService checklistItemService;

    @Autowired
    private AccountRepository accountRepo;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    public Trip convertToEntity(TripDTO dto) {
        Trip trip = new Trip();

        trip.setName(dto.getName());
        trip.setDescription(dto.getDescription());
        trip.setAccounts(dto.getAccounts().stream().map(accountId -> accountRepo.getOne(accountId)).collect(Collectors.toList()));
        trip.setChecklistItems(dto.getItems().stream().map(itemDTO -> checklistItemService.getExistingOrConvert(itemDTO)).collect(Collectors.toList()));

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
