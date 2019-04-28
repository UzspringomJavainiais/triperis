package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "NAME")
    private String name;

    @PositiveOrZero
    @Column(name = "MAX_CAPACITY")
    private Integer maxCapacity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @MapsId(value = "id")
    private Location location;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ApartmentUsage> apartmentUsages = new LinkedList<>();

}
