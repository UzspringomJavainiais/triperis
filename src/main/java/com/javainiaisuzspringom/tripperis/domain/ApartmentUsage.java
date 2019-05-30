package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "apartment_usage")
public class ApartmentUsage implements ConvertableEntity<Integer, ApartmentUsageDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "from_date")
    private Timestamp from;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "to_date")
    private Timestamp to;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @OneToMany(mappedBy = "apartmentUsage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUsage> roomsToUsers = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public void addRoomUsage(RoomUsage roomUsage) {
        roomsToUsers.add(roomUsage);
        roomUsage.setApartmentUsage(this);
    }

    public void removeRoomUsage(RoomUsage roomUsage) {
        roomsToUsers.remove(roomUsage);
        roomUsage.setApartmentUsage(null);
    }

    public ApartmentUsageDTO convertToDTO() {
        ApartmentUsageDTO dto = new ApartmentUsageDTO();

        dto.setId(this.getId());
        dto.setFrom(this.getFrom());
        dto.setTo(this.getTo());
        if (this.getTrip() != null) {
            dto.setTripId(getTrip().getId());
        }
        if(this.getApartment() != null) {
            dto.setApartmentId(this.getApartment().getId());
        }
        if(this.getRoomsToUsers() != null) {
            dto.setRoomsToUsers(getRoomsToUsers().stream().map(RoomUsage::convertToDTO).collect(Collectors.toList()));
        }

        return dto;
    }
}
