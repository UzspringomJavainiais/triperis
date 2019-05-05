package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Maps to {@link StatusCode}
 */
@Data
public class StatusCodeDTO implements ConvertableDTO<Integer>{
    private Integer id;

    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String description;
}
