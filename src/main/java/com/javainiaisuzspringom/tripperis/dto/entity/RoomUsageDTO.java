package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomUsageDTO implements ConvertableDTO<Integer> {

    private Integer id;

    private List<Integer> accountIds = new ArrayList<>();

    private Integer roomId;
}
