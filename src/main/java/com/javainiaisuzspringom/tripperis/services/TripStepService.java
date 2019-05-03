package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.entity.TripStepDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripStepService extends AbstractBasicEntityService<TripStep, TripStepDTO, Integer> {

    @Autowired
    private LocationService locationService;

    protected TripStep convertToEntity(TripStepDTO dto) {
        TripStep tripStep = new TripStep();

        tripStep.setStartDate(dto.getStartDate());
        tripStep.setEndDate(dto.getEndDate());
        tripStep.setOrderNo(dto.getOrderNo());
        tripStep.setName(dto.getName());
        tripStep.setLocation(locationService.getExistingOrConvert(dto.getLocation()));

        return tripStep;
    }
}
