package com.javainiaisuzspringom.tripperis.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.Role;
import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Maps to {@link Role}
 */
@Data
public class RoleDTO implements ConvertableDTO<Integer> {
    private Integer id;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateCreated;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateDeleted;
}
