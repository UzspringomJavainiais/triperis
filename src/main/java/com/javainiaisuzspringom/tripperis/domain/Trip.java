package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trip {

    private long id;
    private String name;
    private StatusCode status;
    private User users;
    private ChecklistItem items;
}
