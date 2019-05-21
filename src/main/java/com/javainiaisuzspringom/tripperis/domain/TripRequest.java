package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.TripRequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "TripRequest")
@Getter
@Setter
@Table(name = "trip_request")
public class TripRequest implements ConvertableEntity<Integer, TripRequestDTO>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "ACCOUNT")
    private Account account;

    @Column(name = "TRIP")
    private Trip trip;

    @Column(name = "STATUS")
    private String status;

    @Override
    public TripRequestDTO convertToDTO() {
        TripRequestDTO tripRequestDTO = new TripRequestDTO();

        tripRequestDTO.setId(getId());
        tripRequestDTO.setAccount(getAccount().convertToDTO());
        tripRequestDTO.setTrip(getTrip().getId());
        tripRequestDTO.setStatus(getStatus());

        return null;
    }
}
