package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApartmentUsageService {

    @Autowired
    private ApartmentUsageRepository apartmentUsageRepository;

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Transactional(propagation = Propagation.REQUIRED)
    public ApartmentUsageDTO save(ApartmentUsageDTO apartmentUsage) {
        ApartmentUsage entityFromDTO = getExistingOrConvert(apartmentUsage);
        ApartmentUsage savedEntity = apartmentUsageRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ApartmentUsageDTO> getAllApartmentUsages() {
        return apartmentUsageRepository.findAll().stream().map(ApartmentUsage::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ApartmentUsage getExistingOrConvert(ApartmentUsageDTO dto) {
        if (dto.getId() != null) {
            Optional<ApartmentUsage> maybeTripStep = apartmentUsageRepository.findById(dto.getId());
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

    private ApartmentUsage convertToEntity(ApartmentUsageDTO dto) {
        ApartmentUsage usage = new ApartmentUsage();

//        usage.setId(dto.getId());
        usage.setFrom(dto.getFrom());
        usage.setTo(dto.getTo());
        usage.setApartment(apartmentRepo.getOne(dto.getApartmentId()));
        usage.setAccounts(dto.getAccountIds().stream().map(accountRepo::getOne).collect(Collectors.toList()));

        return usage;
    }
}
