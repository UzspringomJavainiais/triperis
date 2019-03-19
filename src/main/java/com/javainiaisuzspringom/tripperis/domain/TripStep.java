package com.javainiaisuzspringom.tripperis.domain;

import java.time.LocalDateTime;

/**
 * TripStep.java
 */
public class TripStep
{
    public long id;
    
    public Trip trip;
    
    public String name;
    
    public int orderNo;
    
    LocalDateTime startDate;
    
    LocalDateTime endDate;
    
    public Location location;
    
    
}
