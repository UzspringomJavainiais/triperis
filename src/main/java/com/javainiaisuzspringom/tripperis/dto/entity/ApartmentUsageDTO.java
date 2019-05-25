package com.javainiaisuzspringom.tripperis.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link ApartmentUsage}
 */
@Data
public class ApartmentUsageDTO implements ConvertableDTO<Integer> {
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp from;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp to;

    private Integer apartmentId;

    private List<RoomUsageDTO> roomsToUsers = new ArrayList<>();
}
