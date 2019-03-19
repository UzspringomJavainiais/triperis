
package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apartment {

    private long id;
    private int maxCapacity;
    private Location location;
}
