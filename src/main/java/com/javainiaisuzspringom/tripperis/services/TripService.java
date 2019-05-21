package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.StatusCodeRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService extends AbstractBasicEntityService<Trip, TripDTO, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Getter
    @Autowired
    private TripRepository repository;

    @Autowired
    private TripStepService tripStepService;

    @Autowired
    private ChecklistItemService checklistItemService;

    @Autowired
    private StatusCodeRepository statusCodeRepo;

    @Autowired
    private AccountRepository accountRepo;

    public Trip getTripById(Integer tripId) {
        return repository.getOne(tripId);
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

    protected Trip convertToEntity(TripDTO dto) {
        Trip trip = new Trip();

        trip.setName(dto.getName());
        trip.setDescription(dto.getDescription());
        if (dto.getStatusCode() != null)
            trip.setStatus(statusCodeRepo.getOne(dto.getStatusCode()));
        trip.setAccounts(dto.getAccounts().stream().map(accountId -> accountRepo.getOne(accountId)).collect(Collectors.toList()));
        trip.setItems(dto.getItems().stream().map(itemDTO -> checklistItemService.getExistingOrConvert(itemDTO)).collect(Collectors.toList()));
        trip.setTripSteps(dto.getTripSteps().stream().map(tripStep -> tripStepService.getExistingOrConvert(tripStep)).collect(Collectors.toList()));

        return trip;
    }
}
