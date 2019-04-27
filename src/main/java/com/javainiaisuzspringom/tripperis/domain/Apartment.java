package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class Apartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotNull
    private String name;

    @Column(name = "MAX_CAPACITY")
    @PositiveOrZero
    private Integer capacity;

    @OneToOne(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Integer maxCapacity;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<ApartmentUsage> apartmentUsages = new LinkedList<>();

    public void addChecklistItem(ApartmentUsage usage) {
        apartmentUsages.add(usage);
    }

    public void removeChecklistItem(ApartmentUsage usage) {
        apartmentUsages.remove(usage);
//        usage.setApartment(null);
    }
}
