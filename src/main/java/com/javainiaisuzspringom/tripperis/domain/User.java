package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private long id;
    private String name;
    private String lastName;
    private String password;
    private String email;
    private Trip trips;
    private Role roles;
}
