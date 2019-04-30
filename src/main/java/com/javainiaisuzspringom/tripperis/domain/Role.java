package com.javainiaisuzspringom.tripperis.domain;

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
public class Role implements Serializable {
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
}
