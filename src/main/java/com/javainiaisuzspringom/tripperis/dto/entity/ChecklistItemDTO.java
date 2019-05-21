package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * Maps to {@link ChecklistItem}
 */
public class ChecklistItemDTO implements ConvertableDTO<Integer>{

    @Getter
    @Setter
    private Integer id;

    @Size(max = 100)
    private String name;

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public ChecklistItemDTO setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public ChecklistItemDTO setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }
}
