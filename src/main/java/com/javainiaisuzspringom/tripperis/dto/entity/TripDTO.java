package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStatus;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link Trip}. Used for data transferring and insures no nasty data mangling happens
 */
@Data
public class TripDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String description;

    private TripStatus tripStatus;

    private List<Integer> accounts = new ArrayList<>();

    private List<Integer> organizers = new ArrayList<>();

    private List<ChecklistItemDTO> items = new ArrayList<>();

}
