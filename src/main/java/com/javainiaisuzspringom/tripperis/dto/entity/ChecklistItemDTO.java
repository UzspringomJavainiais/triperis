package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Maps to {@link ChecklistItem}
 */
public class ChecklistItemDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    private boolean isChecked;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

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
