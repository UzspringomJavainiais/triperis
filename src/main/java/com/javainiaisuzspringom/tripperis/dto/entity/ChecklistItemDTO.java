package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Maps to {@link ChecklistItem}
 */
@Data
public class ChecklistItemDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    private Boolean checked;

    private AttachmentDTO attachment;
}
