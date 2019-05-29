package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.Data;

@Data
public class AttachmentDTO {
    private Integer id;

    private String fileName;

    private String extension;

    private byte[] fileData;
}
