package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.TripStepDTO;
import com.javainiaisuzspringom.tripperis.repositories.TripStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripStepService {

    @Autowired
    private TripStepRepository tripStepRepository;

    @Autowired
    private LocationService locationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public TripStepDTO save(TripStepDTO tripStep) {
        TripStep entityFromDTO = getExistingOrConvert(tripStep);
        TripStep savedEntity = tripStepRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<TripStepDTO> getAllTripSteps() {
        return tripStepRepository.findAll().stream()
                .map(TripStep::convertToDTO)
                .collect(Collectors.toList());
    }

    // TODO move this method to some utility class, so it doesn't duplicate itself in all services
    @Transactional
    public TripStep getExistingOrConvert(TripStepDTO dto) {
        if (dto.getId() != null) {
            Optional<TripStep> maybeTripStep = tripStepRepository.findById(dto.getId());
            if (maybeTripStep.isPresent()) {
                return maybeTripStep.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private TripStep convertToEntity(TripStepDTO dto) {
        TripStep tripStep = new TripStep();

//        tripStep.setId(this.getId());
        tripStep.setStartDate(dto.getStartDate());
        tripStep.setEndDate(dto.getEndDate());
        tripStep.setOrderNo(dto.getOrderNo());
        tripStep.setName(dto.getName());
        tripStep.setLocation(locationService.getExistingOrConvert(dto.getLocation()));

        return tripStep;
    }
}
