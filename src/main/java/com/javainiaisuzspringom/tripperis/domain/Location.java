package com.javainiaisuzspringom.tripperis.domain;

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
}
