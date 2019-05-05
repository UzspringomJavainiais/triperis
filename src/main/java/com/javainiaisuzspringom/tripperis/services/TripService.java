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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

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

    public Optional<TripDuration> getTripDuration(TripDTO trip) {
        List<TripDuration> durationList = repository.getDuration(trip.getId());
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
        return repository.findById(id).map(Trip::convertToDTO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeTrip(TripDTO trip) {
        if (repository.existsById(trip.getId())){
            repository.deleteById(trip.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean exists(TripDTO trip) {
        if(repository.existsById(trip.getId())) {
            return true;
        }
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        if(!StringUtils.isEmpty(trip.getDescription())) {
            matcher.withMatcher("description", ignoreCase());
        }
        // TODO improve?

        Trip tripProbe = convertToEntity(trip);
        return repository.exists(Example.of(tripProbe, matcher));
    }

    @Override
    protected JpaRepository<Trip, Integer> getRepository() {
        return null;
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
