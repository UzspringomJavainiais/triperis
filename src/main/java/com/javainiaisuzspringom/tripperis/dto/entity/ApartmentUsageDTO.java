package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * Maps to {@link ApartmentUsage}
 */
@Data
public class ApartmentUsageDTO implements ConvertableDTO<Integer>{
    private Integer id;

    private Timestamp from;

    private Timestamp to;

    private Integer apartmentId;

    private List<Integer> accountIds;
}
