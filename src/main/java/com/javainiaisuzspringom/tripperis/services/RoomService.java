package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Room;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoomRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService extends AbstractBasicEntityService<Room, RoomDTO, Integer> {

    @Autowired
    @Getter
    private RoomRepository repository;

    @Override
    protected Room convertToEntity(RoomDTO dto) {
        Room entity = new Room();

        entity.setMaxCapacity(dto.getMaxCapacity());
        entity.setRoomNumber(dto.getRoomNumber());

        return entity;
    }
}
