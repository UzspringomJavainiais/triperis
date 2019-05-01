package com.javainiaisuzspringom.tripperis.dto;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Maps to {@link ChecklistItem}
 */
@Data
public class ChecklistItemDTO {

    private Integer id;

    @Size(max = 100)
    private String name;

    private boolean isChecked;
}
