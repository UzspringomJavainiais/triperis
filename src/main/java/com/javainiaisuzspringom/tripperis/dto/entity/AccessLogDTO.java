package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AccessLogDTO implements ConvertableDTO<Integer> {
    private Integer id;

    private Timestamp date;

    private AccountDTO account;

    private String type;

    private String action;

    private String roles;
}
