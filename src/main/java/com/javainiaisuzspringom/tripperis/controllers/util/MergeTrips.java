package com.javainiaisuzspringom.tripperis.controllers.util;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
/**
 * Defines a wrapper object for the mergeTrips method
 */
public class MergeTrips {
    private Trip tripOne;
    private Trip tripTwo;

    private String name;
    private String description;
}
