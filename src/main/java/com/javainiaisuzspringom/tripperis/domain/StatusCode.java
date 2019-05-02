package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.StatusCodeDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "status_code")
public class StatusCode implements ConvertableEntity<Integer, StatusCodeDTO>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;

    public StatusCodeDTO convertToDTO() {
        StatusCodeDTO dto = new StatusCodeDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setDescription(this.getDescription());

        return dto;
    }
}
