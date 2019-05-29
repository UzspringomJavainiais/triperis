package com.javainiaisuzspringom.tripperis.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.javainiaisuzspringom.tripperis.domain.ApartmentUsage;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link ApartmentUsage}
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ApartmentUsageDTO implements ConvertableDTO<Integer>{
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp from;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp to;

    private Integer apartmentId;

    @NotEmpty
    @NotNull
    private List<RoomUsageDTO> roomsToUsers = new ArrayList<>();
}
