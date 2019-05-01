package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.domain.Location;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Maps to {@link Location}
 */
@Data
public class LocationDTO {

    private Integer id;

    private String name;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String city;

    private String geocoord;

    @Size(max = 100)
    private String address;
}
