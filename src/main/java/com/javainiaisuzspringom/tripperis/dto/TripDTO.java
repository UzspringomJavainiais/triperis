package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link Trip}. Used for data transferring and insures no nasty data mangling ahappens
 */
@Data
public class TripDTO {

    private Integer id;

    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String description;

    private Integer statusCode;

    private List<Integer> accounts = new ArrayList<>();

    private List<ChecklistItemDTO> items = new ArrayList<>();

    private List<TripStepDTO> tripSteps = new ArrayList<>();
}
