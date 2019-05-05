package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role implements ConvertableEntity<Integer, RoleDTO>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_CREATED")
    private Timestamp dateCreated;

    @Column(name = "DATE_DELETED")
    private Timestamp dateDeleted;

    @ManyToMany(mappedBy = "roles")
    private List<Account> account =  new ArrayList<>();

    public RoleDTO convertToDTO() {
        RoleDTO dto = new RoleDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setDescription(this.getDescription());
        dto.setDateDeleted(this.getDateCreated());
        dto.setDateDeleted(this.getDateDeleted());

        return dto;
    }
}
