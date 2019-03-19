package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Trip {

    private long id;
    private String name;
    private StatusCode status;
    private List<User> users;
    private List<ChecklistItem> items;
}
