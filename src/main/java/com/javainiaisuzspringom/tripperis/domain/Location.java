package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GEOCOORD")
    private String geocoord;

    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;

    @OneToOne
    @MapsId
    private Apartment apartment;

    @Size(max = 100)
    @Column(name = "COUNTRY")
    private String country;

    @Size(max = 100)
    @Column(name = "CITY")
    private String city;
}
