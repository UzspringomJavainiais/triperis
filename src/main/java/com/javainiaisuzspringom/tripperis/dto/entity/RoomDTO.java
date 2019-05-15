package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class RoomDTO implements ConvertableDTO<Integer> {

    private Integer id;

    @NotNull
    @Size(max = 100)
    private String roomNumber;

    @PositiveOrZero
    private Integer maxCapacity;
}
