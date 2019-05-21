package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.RoomUsage;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoomUsageRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RoomUsageService extends AbstractBasicEntityService<RoomUsage, RoomUsageDTO, Integer> {

    @Autowired
    @Getter
    private RoomUsageRepository repository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected RoomUsage convertToEntity(RoomUsageDTO dto) {
        RoomUsage entity = new RoomUsage();

        if(dto.getRoomId() != null) {
            entity.setRoom(roomRepository.getOne(dto.getRoomId()));
        }
        if(dto.getAccountIds() != null) {
            entity.setAccounts(dto.getAccountIds().stream().map(id -> accountRepository.getOne(id)).collect(Collectors.toList()));
        }

        return entity;
    }
}
