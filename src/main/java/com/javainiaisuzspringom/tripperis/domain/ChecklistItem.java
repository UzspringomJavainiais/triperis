package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChecklistItem {
    private long id;
    private String value;
    private Trip trip;
    private Boolean isChecked;
    private Attachments attachments;
}
