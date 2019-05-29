package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.RoomDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Room implements ConvertableEntity<Integer, RoomDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "ROOM_NUMBER")
    private String number;

    @PositiveOrZero
    @Column(name = "MAX_CAPACITY")
    private Integer capacity;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Override
    public RoomDTO convertToDTO() {
        RoomDTO dto = new RoomDTO();

        dto.setId(this.getId());
        dto.setCapacity(this.getCapacity());
        dto.setNumber(this.getNumber());

        return dto;
    }
}
