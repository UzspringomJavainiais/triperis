package com.javainiaisuzspringom.tripperis.dto.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javainiaisuzspringom.tripperis.domain.Apartment;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

/**
 * Maps to {@link Apartment}
 */
@Data
public class ApartmentDTO implements ConvertableDTO<Integer>{
    private Integer id;

    @NotNull
    @Size(max = 100)
    private String name;

    @PositiveOrZero
    private Integer maxCapacity;

    private Integer locationId;

    @JsonManagedReference
    private List<ApartmentUsageDTO> apartmentUsages = new LinkedList<>();
}
