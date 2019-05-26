package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ApartmentUsageService implements BasicDtoToEntityService<ApartmentUsage, ApartmentUsageDTO, Integer> {

    @Getter
    @Autowired
    private ApartmentUsageRepository repository;

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Autowired
    private RoomUsageService roomUsageService;

    @Transactional
    public ApartmentUsage convertToEntity(ApartmentUsageDTO dto) {
        ApartmentUsage usage = new ApartmentUsage();

        usage.setFrom(dto.getFrom());
        usage.setTo(dto.getTo());
        if(dto.getApartmentId() != null)
            usage.setApartment(apartmentRepo.getOne(dto.getApartmentId()));
        usage.setApartment(apartmentRepo.getOne(dto.getApartmentId()));
        usage.setRoomsToUsers(dto.getRoomsToUsers().stream()
                .map(x -> roomUsageService.convertToEntity(x)).collect(Collectors.toList()));

        return usage;
    }

    /**
     * Tests if apartment is valid to be added to apartment
     * @param apartment apartment to test with
     * @param apartmentUsage usage to test
     */
    public void validateUsageToApartment(Apartment apartment, ApartmentUsage apartmentUsage) {

        for(RoomUsage roomUsage: apartmentUsage.getRoomsToUsers()) {
            if(roomUsage.getAccounts().size() != roomUsage.getRoom().getMaxCapacity()) {
                throw new IllegalStateException("Trying to cram too many people into room. Not enough room");
            }
            if(roomUsageService.isAvailableForUsage(roomUsage)) {
                throw new IllegalStateException("Trying to cram too many people into room. Not enough room");
            }
        }
    }
}
