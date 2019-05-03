package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.StatusCodeRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class TripService extends AbstractBasicEntityService<Trip, TripDTO, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripStepService tripStepService;

    @Autowired
    private ChecklistItemService checklistItemService;

    @Autowired
    private StatusCodeRepository statusCodeRepo;

    @Autowired
    private AccountRepository accountRepo;

    public Optional<TripDuration> getTripDuration(TripDTO trip) {
        List<TripDuration> durationList = tripRepository.getDuration(trip.getId());
        if(durationList.isEmpty()) {
            return Optional.empty();
        }
        if(durationList.size() != 1) {
            LOGGER.error("Duration list is not of size 1, but {}", durationList.size());
            throw new IllegalStateException("Illegal attempt to query trips");
        }
        return Optional.of(durationList.get(0));
    }

    public Optional<TripDTO> getById(Integer id) {
        return tripRepository.findById(id).map(Trip::convertToDTO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeTrip(TripDTO trip) {
        if (tripRepository.existsById(trip.getId())){
            tripRepository.deleteById(trip.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean exists(TripDTO trip) {
        if(tripRepository.existsById(trip.getId())) {
            return true;
        }
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        if(!StringUtils.isBlank(trip.getDescription())) {
            matcher.withMatcher("description", ignoreCase());
        }
        // TODO improve?

        Trip tripProbe = convertToEntity(trip);
        return tripRepository.exists(Example.of(tripProbe, matcher));
    }

    protected Trip convertToEntity(TripDTO dto) {
        Trip trip = new Trip();

        trip.setName(dto.getName());
        trip.setDescription(dto.getDescription());
        trip.setStatus(statusCodeRepo.getOne(dto.getStatusCode()));
        trip.setAccounts(dto.getAccounts().stream().map(accountId -> accountRepo.getOne(accountId)).collect(Collectors.toList()));
        trip.setItems(dto.getItems().stream().map(itemDTO -> checklistItemService.getExistingOrConvert(itemDTO)).collect(Collectors.toList()));
        trip.setTripSteps(dto.getTripSteps().stream().map(tripStep -> tripStepService.getExistingOrConvert(tripStep)).collect(Collectors.toList()));

        return trip;
    }
}
