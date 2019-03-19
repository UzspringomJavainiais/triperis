package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    private long id;
    private String name;
    private String lastName;
    private String password;
    private String email;
    private List<Trip> trips;
    private List<Role> roles;
}
