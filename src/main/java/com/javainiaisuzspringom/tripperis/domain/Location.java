package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javainiaisuzspringom.tripperis.dto.entity.LocationDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "location")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location implements ConvertableEntity<Integer, LocationDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;
    
    public LocationDTO convertToDTO() {
        LocationDTO dto = new LocationDTO();

        dto.setId(this.getId());
        dto.setAddress(this.getAddress());
        dto.setCity(this.getCity());
        dto.setCountry(this.getCountry());
        dto.setGeocoord(this.getGeocoord());
        dto.setName(this.getName());

        return dto;
    }

    public Integer getId() {
        return id;
    }

    public Location setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Location setName(String name) {
        this.name = name;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Location setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Location setCity(String city) {
        this.city = city;
        return this;
    }

    public String getGeocoord() {
        return geocoord;
    }

    public Location setGeocoord(String geocoord) {
        this.geocoord = geocoord;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Location setAddress(String address) {
        this.address = address;
        return this;
    }
}
