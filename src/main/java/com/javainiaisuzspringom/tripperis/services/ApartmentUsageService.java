package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ApartmentUsageRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ApartmentUsageService extends AbstractBasicEntityService<ApartmentUsage, ApartmentUsageDTO, Integer> {

    @Getter
    @Autowired
    private ApartmentUsageRepository repository;

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Autowired
    private AccountRepository accountRepo;


    protected ApartmentUsage convertToEntity(ApartmentUsageDTO dto) {
        ApartmentUsage usage = new ApartmentUsage();

        usage.setFrom(dto.getFrom());
        usage.setTo(dto.getTo());
        if(dto.getApartmentId() != null)
            usage.setApartment(apartmentRepo.getOne(dto.getApartmentId()));
        usage.setAccounts(dto.getAccountIds().stream().map(accountRepo::getOne).collect(Collectors.toList()));

        return usage;
    }
}
