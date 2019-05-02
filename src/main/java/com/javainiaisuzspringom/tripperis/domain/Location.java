package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.LocationDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Size(max = 100)
    @Column(name = "COUNTRY")
    private String country;

    @Size(max = 100)
    @Column(name = "CITY")
    private String city;

    @Column(name = "GEOCOORD")
    private String geocoord;

    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;


    public LocationDTO convertToDTO() {
        LocationDTO location = new LocationDTO();

        location.setId(this.getId());
        location.setAddress(this.getAddress());
        location.setCity(this.getCity());
        location.setCountry(this.getCountry());
        location.setGeocoord(this.getGeocoord());
        location.setName(this.getName());

        return location;
    }
}
