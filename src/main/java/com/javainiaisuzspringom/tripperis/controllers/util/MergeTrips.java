package com.javainiaisuzspringom.tripperis.controllers.util;

import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a wrapper object for the mergeTrips method
 */
@Getter @Setter
public class MergeTrips {
    private TripDTO tripOne;
    private TripDTO tripTwo;

    private String name;
    private String description;
}
