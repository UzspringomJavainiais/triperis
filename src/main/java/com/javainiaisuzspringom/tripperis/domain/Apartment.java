package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
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

    @PositiveOrZero
    @Column(name = "MAX_CAPACITY")
    private Integer maxCapacity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<ApartmentUsage> apartmentUsages = new LinkedList<>();

    public ApartmentDTO convertToDTO() {
        ApartmentDTO dto = new ApartmentDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setMaxCapacity(this.getMaxCapacity());
        if(this.getLocation() != null) {
            dto.setLocation(this.getLocation().convertToDTO());
        }
        if(this.getApartmentUsages() != null) {
            dto.setApartmentUsages(this.getApartmentUsages().stream().map(ApartmentUsage::convertToDTO).collect(Collectors.toList()));
        }

        return dto;
    }
}
