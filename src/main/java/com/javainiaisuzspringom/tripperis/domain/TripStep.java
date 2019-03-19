package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripStep {

    private long id;
    private Trip trip;
    private String name;
    private int orderNo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Location location;
}
