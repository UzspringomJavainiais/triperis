package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role implements ConvertableEntity<Integer, RoleDTO>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "DATE_CREATED")
    private Timestamp dateCreated;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "DATE_DELETED")
    private Timestamp dateDeleted;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<Account> account =  new ArrayList<>();

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;

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
