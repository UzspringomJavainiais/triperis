package com.javainiaisuzspringom.tripperis.domain;

import java.time.LocalDateTime;

/**
 * ApartmentUsage.java
 */

public class ApartmentUsage
{
    public User users;
    
    public Apartment apartment;
    
    public long id;
    
    LocalDateTime from;
    
    LocalDateTime to;
}
