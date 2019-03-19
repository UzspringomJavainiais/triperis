package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {

    private long id;
    private String geocoord;
    private String address;
}
