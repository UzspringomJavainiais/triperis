package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Apartment;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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

    private LocationDTO location;

    @NotNull
    private List<ApartmentUsageDTO> apartmentUsages = new ArrayList<>();
}
