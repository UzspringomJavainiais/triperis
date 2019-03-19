package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApartmentUsage {
    private User users;
    private Apartment apartment;
    private long id;
    private LocalDateTime from;
    private LocalDateTime to;
}
