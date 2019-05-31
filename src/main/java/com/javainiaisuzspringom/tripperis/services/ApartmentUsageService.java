package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.*;
import com.javainiaisuzspringom.tripperis.dto.ReservationInfo;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        if(dto.getApartmentId() != null)
            usage.setApartment(apartmentRepo.getOne(dto.getApartmentId()));
        dto.getRoomsToUsers().stream()
                .map(x -> roomUsageService.convertToEntity(x))
                .forEach(usage::addRoomUsage);

        return usage;
    }


    public Pair<List<RoomUsage>, List<Account>> autoAssignRooms(ApartmentUsage apartmentUsage, List<Account> accounts) {
//        apartmentUsage.getApartment().getRooms()
        return Pair.of(Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Tests if apartment is valid to be added to apartment
     * @param apartmentUsage usage to test
     */
    public void validateUsageToApartment(ApartmentUsage apartmentUsage) {

        for(RoomUsage roomUsage: apartmentUsage.getRoomsToUsers()) {
            if(roomUsage.getAccounts().size() > roomUsage.getRoom().getCapacity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trying to cram too many people into room. Not enough room");
            }
            if(!roomUsageService.isAvailableForUsage(roomUsage)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trying to cram too many people into room. Not enough room");
            }
        }
    }

    public List<ReservationInfo> getCapacityListForRoom(Room room, Date dateStart, Date dateEnd) {
        return roomUsageService.getReservedCapacities(room, Timestamp.from(dateStart.toInstant()), Timestamp.from(dateEnd.toInstant()));
    }

    public List<ApartmentUsage> findAllApartmentUsagesInPeriod(Apartment apartment, Date dateStart, Date dateEnd) {
        return repository.findAllApartmentUsagesInPeriod(apartment, Timestamp.from(dateStart.toInstant()), Timestamp.from(dateEnd.toInstant()));
    }
}
