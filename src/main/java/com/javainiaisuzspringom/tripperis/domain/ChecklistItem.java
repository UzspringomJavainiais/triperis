package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChecklistItem {

    private long id;
    private String value;
    private Trip trip;
    private Boolean isChecked;
    private List<Attachments> attachments;
}
