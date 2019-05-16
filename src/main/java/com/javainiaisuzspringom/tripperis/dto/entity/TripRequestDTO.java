package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

@Data
public class TripRequestDTO implements ConvertableDTO<Integer> {
    private Integer id;

    private AccountDTO account;

    private Integer trip;

    private String status;
}
