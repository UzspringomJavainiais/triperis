package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "apartment")
public class Apartment implements ConvertableEntity<Integer, ApartmentDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<ApartmentUsage> apartmentUsages = new ArrayList<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;

    public ApartmentDTO convertToDTO() {
        ApartmentDTO dto = new ApartmentDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        if(this.getLocation() != null) {
            dto.setLocation(this.getLocation().convertToDTO());
        }
        if(this.getApartmentUsages() != null) {
            dto.setApartmentUsages(this.getApartmentUsages().stream().map(ApartmentUsage::convertToDTO).collect(Collectors.toList()));
        }
        if(this.getRooms() != null) {
            dto.setRooms(this.getRooms().stream().map(Room::convertToDTO).collect(Collectors.toList()));
        }

        return dto;
    }

    public void addApartmentUsage(ApartmentUsage usage) {
        if(usage != null && !apartmentUsages.contains(usage)) {
            apartmentUsages.add(usage);
            usage.setApartment(this);
        }
    }

    public void removeApartmentUsage(ApartmentUsage usage) {
        if(usage != null && apartmentUsages.contains(usage)) {
            apartmentUsages.remove(usage);
            usage.setApartment(null);
        }
    }

    public void addRoom(Room room) {
        if(room != null && !rooms.contains(room)) {
            rooms.add(room);
            room.setApartment(this);
        }
    }

    public void removeRoom(Room room) {
        if(room != null && rooms.contains(room)) {
            rooms.remove(room);
            room.setApartment(null);
        }
    }
}
