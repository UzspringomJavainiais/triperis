package com.javainiaisuzspringom.tripperis.controllers.util;

import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a wrapper object for the mergeTrips method
 */
public class MergeTrips {
    private TripDTO tripOne;
    private TripDTO tripTwo;

    private String name;
    private String description;

    public TripDTO getTripOne() {
        return tripOne;
    }

    public MergeTrips setTripOne(TripDTO tripOne) {
        this.tripOne = tripOne;
        return this;
    }

    public TripDTO getTripTwo() {
        return tripTwo;
    }

    public MergeTrips setTripTwo(TripDTO tripTwo) {
        this.tripTwo = tripTwo;
        return this;
    }

    public String getName() {
        return name;
    }

    public MergeTrips setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MergeTrips setDescription(String description) {
        this.description = description;
        return this;
    }
}
