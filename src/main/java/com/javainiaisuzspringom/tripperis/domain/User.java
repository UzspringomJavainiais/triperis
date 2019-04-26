package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "account")
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private String lastName;

    private String password;

    @Email
    private String email;

    @ManyToMany(mappedBy = "users")
    private Set<Trip> trips;

    @ManyToMany
    private Set<Role> roles;
}
