package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Maps to {@link com.javainiaisuzspringom.tripperis.domain.TripStep}
 */
@Data
public class TripStepDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    private Integer orderNo;

    private Timestamp startDate;

    private Timestamp endDate;

    private LocationDTO location;
}
